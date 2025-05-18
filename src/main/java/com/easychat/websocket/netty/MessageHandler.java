package com.easychat.websocket.netty;

import com.easychat.entity.dto.MessageSendDto;
import com.easychat.utils.JsonUtils;
import com.easychat.websocket.ChannelContextUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @ClassName MessageHandler
 * @Author chenhongxin
 * @Date 2025/5/18 下午4:49
 * @mood happy
 * @Description 消息处理器
 */
@Component("messageHandler")
public class MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private static final String MESSAGE_TOPIC = "message.topic";


    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ChannelContextUtils channelContextUtils;

    @PostConstruct
    public void lisMessage() {
        RTopic rtopic = redissonClient.getTopic(MESSAGE_TOPIC);
        rtopic.addListener(MessageSendDto.class,(MessageSendDto,sendDto)->{
            logger.info("收到广播消息：{}", JsonUtils.convertObj2Json(sendDto));
            channelContextUtils.sendMessage(sendDto);
        });
    }

    public void sendMessage(MessageSendDto sendDto) {
        RTopic rtopic = redissonClient.getTopic(MESSAGE_TOPIC);
        rtopic.publish(sendDto);
    }
}
