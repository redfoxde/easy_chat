package com.easychat.redis;


import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


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

    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto){
        redisUtils.set(constants.REDIS_WS_TOKEN+tokenUserInfoDto.getToken(), String.valueOf(tokenUserInfoDto),constants.REDIS_KEY_EXPRESS_DAY*2);
        redisUtils.set(constants.REDIS_WS_TOKEN_USERID+tokenUserInfoDto.getToken(), tokenUserInfoDto.getToken(),constants.REDIS_KEY_EXPRESS_DAY*2);

    }

    public SysSettingDto getSysSettingDto(){
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(constants.REDIS_SYS_SETTING);
        sysSettingDto = sysSettingDto == null ? new SysSettingDto() : sysSettingDto;
        return sysSettingDto;
    }

}
