package com.easychat.aspect;

import com.easychat.redis.RedisUtils;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName GlobalOperationAspect
 * @Author chenhongxin
 * @Date 2025/5/7 下午5:53
 * @mood happy
 */
@Aspect
@Component("globalOperationAspect")
public class GlobalOperationAspect {
    @Resource
    private RedisUtils redisUtils;

    private static final Logger logger = LoggerFactory.getLogger(GlobalOperationAspect.class);
}
