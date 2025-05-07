package com.easychat.entity.enums;

/**
 * @ClassName BeautyAccountStatusEnum
 * @Author chenhongxin
 * @Date 2025/5/6 下午6:10
 * @mood happy
 */
public enum BeautyAccountStatusEnum {
    NO_USE(0,"未使用"),
    USE(1,"已使用");
    private Integer status;
    private String desc;

    private BeautyAccountStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static BeautyAccountStatusEnum getByStatus(Integer status) {
        for (BeautyAccountStatusEnum item : BeautyAccountStatusEnum.values()) {
            if (item.status.equals(status)) {
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
