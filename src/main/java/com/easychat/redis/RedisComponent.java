package com.easychat.redis;

import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.utils.StringTools;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component("redisComponent")
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    // ------------------------- 心跳相关 -------------------------

    public Long getUserHeartBeat(String userId) {
        return redisUtils.get(constants.REDIS_USER_HEART_BEAT + userId, Long.class);
    }

    public void saveHeartBeat(String userId) {
        redisUtils.set(constants.REDIS_USER_HEART_BEAT + userId,
                System.currentTimeMillis(),
                constants.REDIS_KEY_EXPRESS_HEART_BEAT,
                TimeUnit.SECONDS);
    }

    public void removeUserHeartBeat(String userId) {
        redisUtils.delete(constants.REDIS_USER_HEART_BEAT + userId);
    }

    // ------------------------- Token 用户信息 -------------------------

    public TokenUserInfoDto getTokenUserInfoDto(String token) {
        return redisUtils.get(constants.REDIS_WS_TOKEN + token, TokenUserInfoDto.class);
    }

    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto) {
        redisUtils.set(
                constants.REDIS_WS_TOKEN + tokenUserInfoDto.getToken(),
                tokenUserInfoDto,
                constants.REDIS_KEY_EXPRESS_DAY * 2,
                TimeUnit.DAYS
        );

        redisUtils.set(
                constants.REDIS_WS_TOKEN_USERID + tokenUserInfoDto.getUserId(),
                tokenUserInfoDto.getToken(),
                constants.REDIS_KEY_EXPRESS_DAY * 2,
                TimeUnit.DAYS
        );
    }

    // ------------------------- 系统设置 -------------------------

    public SysSettingDto getSysSettingDto() {
        SysSettingDto setting = redisUtils.get(constants.REDIS_SYS_SETTING, SysSettingDto.class);
        return setting != null ? setting : new SysSettingDto();
    }

    public void saveSysSetting(SysSettingDto sysSettingDto) {
        redisUtils.set(constants.REDIS_SYS_SETTING, sysSettingDto);
    }

    // ------------------------- 用户联系人 -------------------------

    public void cleanUserContact(String userId) {
        redisUtils.delete(constants.REDIS_KEY_USER_CONTACT + userId);
    }

    public void addUserContactBatch(String userId, List<String> contactIdList) {
        redisUtils.batchPushContacts(constants.REDIS_KEY_USER_CONTACT + userId,
                contactIdList,
                constants.REDIS_KEY_TOKEN_EXPIRES);
    }

    public void addUserContact(String userId, String contactId) {
        List<String> contactIdList = getUserContactList(userId);
        if (contactIdList.contains(contactId)) {
            return;
        }
        redisUtils.batchPushContacts(constants.REDIS_KEY_USER_CONTACT + userId,
                Collections.singletonList(contactId),
                constants.REDIS_KEY_TOKEN_EXPIRES);
    }

    public List<String> getUserContactList(String userId) {
        return redisUtils.range(constants.REDIS_KEY_USER_CONTACT + userId,
                0, -1, String.class);
    }

    public void cleanUserTokenByUserId(String userId) {
        String token  = redisUtils.get(constants.REDIS_WS_TOKEN_USERID + userId);
        if(StringTools.isEmpty(token)){
            return;
        }
        redisUtils.delete(constants.REDIS_WS_TOKEN + token);
    }
}