package com.easychat.entity.enums;

import com.easychat.utils.StringTools;

/**
 * @ClassName JoinType
 * @Author chenhongxin
 * @Date 2025/5/7 下午3:35
 * @mood happy
 */
public enum JoinTypeEnum {
    JOIN(0,"直接加入"),
    APPLY(1,"需要审核");

    private Integer type;
    private String desc;
    JoinTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }

    public static JoinTypeEnum getByName(String name) {
        try{
            if (StringTools.isEmpty(name)) {
                return null;
            }
            return JoinTypeEnum.valueOf(name.toUpperCase());
        }catch (IllegalArgumentException e){
            return null;
        }
    }
    public static JoinTypeEnum setByType(Integer type) {
        for (JoinTypeEnum joinTypeEnum : JoinTypeEnum.values()) {
            if (joinTypeEnum.getType().equals(type)) {
                return joinTypeEnum;
            }
        }
        return null;
    }
}
