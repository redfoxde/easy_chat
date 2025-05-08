package com.easychat.entity.enums;

import com.easychat.utils.StringTools;

/**
 * @ClassName UserContactStatusEnum
 * @Author chenhongxin
 * @Date 2025/5/8 上午10:44
 * @mood happy
 */
public enum UserContactStatusEnum {
    NOT_FRIEND(0,"非好友"),
    FRIEND(1,"好友"),
    DEL(2,"已删除好友"),
    DEL_BE(3,"被删除好友"),
    BLACKLIST(4,"拉黑"),
    BLACKLIST_BE(5,"被拉黑");

    private Integer status;
    private String desc;

    UserContactStatusEnum(Integer status,String desc){
        this.status = status;
        this.desc = desc;
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

    public static UserContactStatusEnum getByStatus(String status){
        try{
            if(StringTools.isEmpty(status)){
                return null;
            }
            return  UserContactStatusEnum.valueOf(status.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    public static UserContactStatusEnum getByStatus(Integer status){
        for(UserContactStatusEnum item : UserContactStatusEnum.values()){
            if(item.getStatus().equals(status)){
                return item;
            }
        }
        return null;
    }
}
