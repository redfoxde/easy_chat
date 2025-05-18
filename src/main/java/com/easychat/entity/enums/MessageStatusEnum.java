package com.easychat.entity.enums;

/**
 * @ClassName MessageStatusEnum
 * @Author chenhongxin
 * @Date 2025/5/15 下午1:36
 * @mood happy
 */
public enum MessageStatusEnum {
    SENDING(0, "发送中"),
    SENT(1, "已发送");

    private final Integer status;
    private String desc;

    MessageStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MessageStatusEnum getByStatus(Integer status) {
        for (MessageStatusEnum item : MessageStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
