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

    public static final Integer REDIS_KEY_EXPRESS_HEART_BEAT = 6;

    public static final String REDIS_USER_HEART_BEAT= "easychat:ws:user:heartbeat";
    /**
     * 存Token
     */
    public static final String REDIS_WS_TOKEN= "easychat:ws:token";

    public static final String REDIS_WS_TOKEN_USERID= "easychat:ws:token:userid";

    public static final Integer Time_IMIN = 60;

    public static final Integer REDIS_KEY_EXPRESS_DAY = Time_IMIN*60*24;

    //token失效时间
    public static final Integer REDIS_KEY_TOKEN_EXPIRES = REDIS_KEY_EXPRESS_DAY*2;


    public static final Integer Length_11= 11;

    public static final Integer Length_20= 20;

    public static final String ROBOT_UID = UserContactTypeEnum.USER.getPrefix()+"robot";

    public static final String REDIS_SYS_SETTING = "easychat:sys:setting";

    public static final String FILE_FOLDER_FILE = "/file/";

    public static final String FILE_FOLDER_AVATAR_NAME = "/avatar/";

    public static final String IMAGE_SUFFIX = ".png";

    public static final String COVER_IMAGE_SUFFIX = "_cover.png";

    public static final String APPLY_INFO_TEMPLATE = "我是%s";

    public static final String REGEX_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,12}$";

    //用户联系人列表
    public static final String REDIS_KEY_USER_CONTACT = "easychat:ws:user:contact";

}
