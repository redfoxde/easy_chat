package com.easychat.websocket;

import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.WsInitData;
import com.easychat.entity.enums.MessageTypeEnum;
import com.easychat.entity.enums.UserContactApplyStatusEnum;

import com.easychat.entity.enums.UserContactTypeEnum;
import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.po.ChatSessionUser;
import com.easychat.entity.po.UserContactApply;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.ChatMessageQuery;
import com.easychat.entity.query.ChatSessionUserQuery;
import com.easychat.entity.query.UserContactApplyQuery;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.mappers.ChatMessageMapper;
import com.easychat.mappers.UserContactApplyMapper;
import com.easychat.mappers.UserInfoMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.service.ChatSessionUserService;
import com.easychat.utils.JsonUtils;
import com.easychat.utils.StringTools;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName ChannelContextUtils
 * @Author chenhongxin
 * @Date 2025/5/14 下午4:26
 * @mood happy
 */
@Component
public class ChannelContextUtils {

    private static final Logger logger = LoggerFactory.getLogger(ChannelContextUtils.class);
    /**
     * 单聊
     */
    private static final ConcurrentHashMap<String, Channel> USER_CONTACT_MAP = new ConcurrentHashMap<>();
    /**
     * 群聊
     */
    private static final ConcurrentHashMap<String, ChannelGroup> GROUP_CONTACT_MAP = new ConcurrentHashMap<>();

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private ChatSessionUserService chatSessionUserService;

    @Resource
    private ChatMessageMapper<ChatMessage,ChatMessageQuery> chatMessageMapper;

    @Resource
    private UserContactApplyMapper<UserContactApply,UserContactApplyQuery> userContactApplyMapper;

    public void addContext(String userId, Channel channel){


        String channelId = channel.id().toString();
        logger.info("add channel id:" + channelId);
        AttributeKey attributeKey = null;
        if(!AttributeKey.exists(channelId)){
            attributeKey = AttributeKey.newInstance(channelId);
        }else{
            attributeKey = AttributeKey.valueOf(channelId);
        }

        channel.attr(attributeKey).set(userId);

        List<String> contactIdList = redisComponent.getUserContactList(userId);

        for(String groupId : contactIdList){
            if(groupId.startsWith(UserContactTypeEnum.GROUP.getPrefix())){
                add2Group(groupId, channel);
            }
        }

        USER_CONTACT_MAP.put(userId, channel);
        redisComponent.saveHeartBeat(userId);

        //更新用户最后连接时间
        UserInfo updateInfo = new UserInfo();
        updateInfo.setLastLoginTime(new Date());
        userInfoMapper.updateByUserId(updateInfo,userId);

        //给用户发消息
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        Long sourceLastOfTime = userInfo.getLastOffTime();
        Long lastOffTime = sourceLastOfTime;
        if(sourceLastOfTime != null && System.currentTimeMillis()- constants.MILLIS_SECONDS_3DAYS_AGO >sourceLastOfTime){
            lastOffTime = constants.MILLIS_SECONDS_3DAYS_AGO;

        }
        /**
         * 查询会话信息 查询用户会话信息，保证用户换了设备信息仍然存在
         */
        ChatSessionUserQuery sessionUserQuery = new ChatSessionUserQuery();
        sessionUserQuery.setUserId(userId);
        sessionUserQuery.setOrderBy("last_receive_time desc");
        List<ChatSessionUser> chatSessionUserList = chatSessionUserService.findListByParam(sessionUserQuery);

        WsInitData wsInitData = new WsInitData();
        wsInitData.setChatSessionList(chatSessionUserList);
        /**
         * 查询聊天消息
         */
        //查询所有的联系人
        List<String> groupIdList = contactIdList.stream().filter(item->item.startsWith(UserContactTypeEnum.GROUP.getPrefix())).collect(Collectors.toList());
        groupIdList.add(userId);
        ChatMessageQuery messageQuery = new ChatMessageQuery();
        messageQuery.setContactIdList(groupIdList);
        messageQuery.setLastReceiveTime(lastOffTime);
        List<ChatMessage> chatMessageList = chatMessageMapper.selectList(messageQuery);
        wsInitData.setChatMessagesList(chatMessageList);


        /**
         * 查询好友申请
         */
        UserContactApplyQuery applyQuery = new UserContactApplyQuery();
        applyQuery.setReceiveUserId(userId);
        applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
        applyQuery.setLastApplyTime(lastOffTime);
        Integer applyCount = userContactApplyMapper.selectCount(applyQuery);
        wsInitData.setApplyCount(applyCount);


        //发送消息
        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageType(MessageTypeEnum.INIT.getType());
        messageSendDto.setContactId(userId);
        messageSendDto.setExtendData(wsInitData);
        sendMsg(messageSendDto,userId);
    }

    public void addUser2Group(String userId, String groupId) {
        Channel channel = USER_CONTACT_MAP.get(userId);
        add2Group(groupId, channel);
    }


    private void add2Group(String groupId, Channel channel){
        ChannelGroup group = GROUP_CONTACT_MAP.get(groupId);

        if(group == null){
            group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            GROUP_CONTACT_MAP.put(groupId, group);
        }
        if(channel == null){
            return;
        }
        group.add(channel);
    }

    public void removeContext(Channel channel){
        Attribute<String> attribute = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        if(StringTools.isEmpty(userId)){
            USER_CONTACT_MAP.remove(userId);
        }
        redisComponent.removeUserHeartBeat(userId);
        //更新用户最后离线时间
        UserInfo userInfo = new UserInfo();
        userInfo.setLastOffTime(System.currentTimeMillis());
        userInfoMapper.updateByUserId(userInfo,userId);
    }

    public void sendMessage(MessageSendDto messageSendDto){
        UserContactTypeEnum contactTypeEnum = UserContactTypeEnum.getByPrefix(messageSendDto.getContactId());
        switch(contactTypeEnum){
            case USER:
                send2User(messageSendDto);
                break;
            case GROUP:
                send2Group(messageSendDto);
                break;
        }
    }

    //发送给用户
    private void send2User(MessageSendDto messageSendDto){
        String contactId = messageSendDto.getContactId();
        if(StringTools.isEmpty(contactId)){
            return;
        }
        sendMsg(messageSendDto,contactId);
        //强制下线
        if(MessageTypeEnum.FORCE_OFFLINE.getType().equals(messageSendDto.getMessageType())){
            closeContext(contactId);
        }
    }

    public void closeContext(String userId){
        if(StringTools.isEmpty(userId)){
            return;
        }
        redisComponent.cleanUserTokenByUserId(userId);
        Channel channel = USER_CONTACT_MAP.get(userId);
        if(channel == null){
            return;
        }
        channel.close();
    }


    //发送给群聊
    private void send2Group(MessageSendDto messageSendDto){
        if(StringTools.isEmpty(messageSendDto.getContactId())){
            return;
        }
        ChannelGroup channelGroup = GROUP_CONTACT_MAP.get(messageSendDto.getContactId());
        if(channelGroup == null){
            return;
        }
        channelGroup.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObj2Json(messageSendDto)));
    }

    //发送消息
    public  void sendMsg(MessageSendDto messageSendDto,String receiveId){
        Channel userchannel = USER_CONTACT_MAP.get(receiveId);
        if(userchannel == null){
            return;
        }
        //相对于客户端而言，联系人就是发送人
        if(MessageTypeEnum.ADD_FRIEND_SELF.getType().equals(messageSendDto.getMessageType())){
            UserInfo userInfo = (UserInfo) messageSendDto.getExtendData();
            messageSendDto.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
            messageSendDto.setContactId(userInfo.getUserId());
            messageSendDto.setContactName(userInfo.getNickName());
            messageSendDto.setExtendData(null);
        }else{
            messageSendDto.setContactId(messageSendDto.getSendUserId());
            messageSendDto.setContactName(messageSendDto.getSendUseNickName());
        }


        userchannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.convertObj2Json(messageSendDto)));


    }


}
