package com.easychat.entity.enums;

/**
 * @ClassName GroupStatusEnum
 * @Author chenhongxin
 * @Date 2025/5/8 下午1:43
 * @mood happy
 */
public enum GroupStatusEnum {
    NORMAL(0,"正常"),
    DISSOLUTION(1,"群聊已解散");

    private Integer status;
    private String desc;

    GroupStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public static GroupStatusEnum getByStatus(Integer status) {
        for (GroupStatusEnum groupStatusEnum : GroupStatusEnum.values()) {
            if (groupStatusEnum.status == status) {
                return groupStatusEnum;
            }
        }
        return null;
    }
    public Integer getStatus() {
        return status;
    }
}
