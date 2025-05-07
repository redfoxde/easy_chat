package com.easychat.entity.constants;

import com.easychat.entity.enums.UserContactTypeEnum;

/**
 * @ClassName constants
 * @Author chenhongxin
 * @Date 2025/5/6 下午4:38
 * @mood happy
 */
public class constants {
    public static final String EASY_CHAT = "easychat:checkcode";

    public static final String REDIS_USER_HEART_BEAT= "easychat:ws:user:heartbeat";
    /**
     * 存Token
     */
    public static final String REDIS_WS_TOKEN= "easychat:ws:token";

    public static final String REDIS_WS_TOKEN_USERID= "easychat:ws:token:userid";

    public static final Integer Time_IMIN = 60;

    public static final Integer REDIS_KEY_EXPRESS_DAY = Time_IMIN*60*24;

    public static final Integer Length_11= 11;

    public static final Integer Length_20= 20;

    public static final String ROBOT_UID = UserContactTypeEnum.USER.getPrefix()+"robot";

    public static final String REDIS_SYS_SETTING = "easychat:sys:setting";
}
