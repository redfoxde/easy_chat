package com.easychat.websocket.netty;

import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.redis.RedisComponent;
import com.easychat.utils.StringTools;
import com.easychat.websocket.ChannelContextUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName HandlerWebSocket
 * @Author chenhongxin
 * @Date 2025/5/13 下午7:20
 * @mood ┭┮﹏┭┮
 */
@Component
@ChannelHandler.Sharable
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private ChannelContextUtils channelContextUtils;

    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket.class);
    /**
     * 通道就绪后，调用一般用户初始化
     *
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有新的连接加入......");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有连接断开......");
        channelContextUtils.removeContext(ctx.channel());
    }

    /**
     *
     * 读消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = ctx.channel();
        Attribute<String> attribute =  channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attribute.get();
        //logger.info("收到userId{}的消息:{}", userId,textWebSocketFrame.text());
        redisComponent.saveHeartBeat(userId);

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete complete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;

            String url = complete.requestUri();
            String token = getToken(url);
            logger.info("握手请求中提取到 token = {}", token);
            if(token == null) {
                logger.warn("token 为空，关闭连接");
                ctx.channel().close();
                return;
            }
            logger.info("WebSocket 握手完成事件触发, uri={}", ((WebSocketServerProtocolHandler.HandshakeComplete) evt).requestUri());


            TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDto(token);
            logger.info("Redis 返回的 tokenUserInfoDto = {}", tokenUserInfoDto);
            if(tokenUserInfoDto == null) {
                logger.warn("token 无效或过期，关闭连接");
                ctx.channel().close();
                return;
            }
            //聊天
            channelContextUtils.addContext(tokenUserInfoDto.getUserId(),ctx.channel());
        }
    }

    private String getToken(String url) {
        if (StringTools.isEmpty(url) || !url.contains("?")) {
            return null;
        }

        String queryString = url.substring(url.indexOf("?") + 1);
        String[] params = queryString.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && "token".equals(keyValue[0])) {
                return keyValue[1];
            }
        }
        return null;
    }
}
