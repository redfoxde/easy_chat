package com.easychat.redis;



import com.easychat.entity.constants.constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnMessage;

/**
 * @ClassName RedisComponent
 * @Author chenhongxin
 * @Date 2025/5/7 下午2:03
 * @mood happy
 */

@Component("redisComponent")
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    /**
     *
     * 获取心跳
     */
    public Long getUserHeartBeat(String userId){
        return (Long) redisUtils.get(constants.REDIS_USER_HEART_BEAT+userId);
    }

}
