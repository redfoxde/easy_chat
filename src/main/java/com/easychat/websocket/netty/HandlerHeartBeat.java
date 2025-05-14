package com.easychat.websocket.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName HandlerHeartBeat
 * @Author chenhongxin
 * @Date 2025/5/13 下午7:08
 * @mood happy
 */
@Component
public class HandlerHeartBeat extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(HandlerHeartBeat.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state()== IdleState.READER_IDLE){
                logger.info("心跳超时");
                ctx.channel().close();
            }else if(event.state()== IdleState.WRITER_IDLE){
                ctx.writeAndFlush("heart");

            }
        }
    }
}
