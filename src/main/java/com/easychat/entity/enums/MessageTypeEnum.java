package com.easychat.entity.enums;

/**
 * @ClassName MessageTypeEum
 * @Author chenhongxin
 * @Date 2025/5/15 下午12:39
 * @mood happy
 */
public enum MessageTypeEnum {
    INIT(0, "", "连接WebSocket获取信息"),
    ADD_FRIEND(1, "你好，我想添加你为好友", "添加好友打招呼消息"),
    CHAT(2, "", "普通聊天消息"),
    GROUP_CREATE(3, "群组已创建，可以和好友畅聊了", "群创建成功"),
    CONTACT_APPLY(4, "", "好友申请"),
    MEDIA_CHAT(5, "", "媒体文件"),
    FILE_UPLOAD(6, "", "文件上传完成"),
    FORCE_OFFLINE(7, "", "强制下线"),
    DISSOLUTION_GROUP(8, "群聊已解散", "解散群聊"),
    ADD_GROUP(9, "%s加入了群组", "加入群聊"),
    GROUP_NAME_UPDATE(10, "", "更新群昵称"),
    LEAVE_GROUP(11, "%s退出了群聊", "退出群聊"),
    REMOVE_GROUP(12, "%s被管理员移出群聊", "被移出群聊"),
    ADD_FRIEND_SELF(13,"","添加好友打招呼消息");

    private final Integer type;
    private final String initMessage;
    private final String desc;

    MessageTypeEnum(Integer type, String initMessage, String desc) {
        this.type = type;
        this.initMessage = initMessage;
        this.desc = desc;
    }


    // Getters
    public Integer getType() { return type; }
    public String getInitMessage() { return initMessage; }
    public String getDesc() { return desc; }

    public static MessageTypeEnum getByType(Integer type) {
        for (MessageTypeEnum item : MessageTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

}
