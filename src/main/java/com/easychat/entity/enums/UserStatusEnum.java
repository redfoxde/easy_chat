package com.easychat.entity.enums;

import com.easychat.utils.StringTools;

/**
 * @ClassName UserStatusEnum
 * @Author chenhongxin
 * @Date 2025/5/6 下午6:32
 * @mood happy
 */
public enum UserStatusEnum {
    DISABLE(0,"账号已禁用"),
    ENABLE(1,"账户已存在");

    private Integer status;
    private String desc;

    UserStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static UserStatusEnum getUserStatusEnum(Integer status) {
        for (UserStatusEnum item : UserStatusEnum.values()) {
            if (item.status.equals(status)) {
                return item;
            }
        }
        return null;
    }
    public static UserStatusEnum getByStatus(Integer status){
        for(UserStatusEnum item : UserStatusEnum.values()){
            if(item.getStatus().equals(status)){
                return item;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

}
