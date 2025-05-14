package com.easychat.websocket;

import com.easychat.entity.enums.UserContactStatusEnum;
import com.easychat.entity.enums.UserContactTypeEnum;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.mappers.UserInfoMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.websocket.netty.HandlerWebSocket;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
        UserInfo userInfo = new UserInfo();
        userInfo.setLastLoginTime(new Date());
        userInfoMapper.updateByUserId(userInfo,userId);


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

}
