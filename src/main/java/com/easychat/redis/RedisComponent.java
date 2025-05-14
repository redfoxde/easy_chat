package com.easychat.redis;


import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


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

    public void saveHeartBeat(String userId){
        redisUtils.set(constants.REDIS_USER_HEART_BEAT+userId, String.valueOf(System.currentTimeMillis()),constants.REDIS_KEY_EXPRESS_HEART_BEAT);
    }

    public TokenUserInfoDto getTokenUserInfoDto(String token){
        TokenUserInfoDto tokenUserInfoDto =(TokenUserInfoDto) redisUtils.get(constants.REDIS_WS_TOKEN+token);
        return tokenUserInfoDto;
    }

    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto){
        redisUtils.set(constants.REDIS_WS_TOKEN+tokenUserInfoDto.getToken(), String.valueOf(tokenUserInfoDto),constants.REDIS_KEY_EXPRESS_DAY*2);
        redisUtils.set(constants.REDIS_WS_TOKEN_USERID+tokenUserInfoDto.getUserId(), tokenUserInfoDto.getToken(),constants.REDIS_KEY_EXPRESS_DAY*2);

    }

    public SysSettingDto getSysSettingDto(){
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(constants.REDIS_SYS_SETTING);
        sysSettingDto = sysSettingDto == null ? new SysSettingDto() : sysSettingDto;
        return sysSettingDto;
    }

    public void saveSysSetting(SysSettingDto sysSettingDto){
        redisUtils.set(constants.REDIS_SYS_SETTING, String.valueOf(sysSettingDto));
    }

    //清空联系人
    public void cleanUserContact(String userId){
        redisUtils.delete(constants.REDIS_KEY_USER_CONTACT+userId);
    }

    //批量添加联系人
    public void addUserContactBatch(String userId, List<String> contanctIdList){
        redisUtils.lpushAll(constants.REDIS_KEY_USER_CONTACT+userId, contanctIdList,constants.REDIS_KEY_TOKEN_EXPIRES);

    }

    //批量添加联系人
    public List<String> getUserContactList(String userId){
        return (List<String>) redisUtils.get(constants.REDIS_KEY_USER_CONTACT+userId);

    }

}
