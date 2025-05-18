package com.easychat.entity.dto;

import com.easychat.utils.StringTools;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @ClassName MessageSendDto
 * @Author chenhongxin
 * @Date 2025/5/15 下午12:23
 * @mood happy
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageSendDto<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //消息ID
    private Long messageId;
    //会话ID
    private String sessionId;
    //发送人
    private String sendUserId;
    //发送人昵称
    private String sendUseNickName;
    //联系人ID
    private String contactId;
    //联系人名称
    private String contactName;
    //消息内容
    private String messageContent;
    //最后的消息
    private String lastMessage;
    //消息类型
    private Integer MessageType;
    //发送时间
    private Long sendTime;
    //联系人类型
    private Integer contactType;
    //扩展消息
    private T extendData;
    //消息类型 0:发送中 1:已发送 对于文件是异步上传用状态处理
    private Integer status;

    //文件信息
    private Long fileSize;
    private String fileName;
    private Integer fileType;

    //群员
    private Integer memberCount;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUseNickName() {
        return sendUseNickName;
    }

    public void setSendUseNickName(String sendUseNickName) {
        this.sendUseNickName = sendUseNickName;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getLastMessage() {
        if(StringTools.isEmpty(lastMessage)){
            return messageContent;
        }
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Integer getMessageType() {
        return MessageType;
    }

    public void setMessageType(Integer messageType) {
        MessageType = messageType;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public T getExtendData() {
        return extendData;
    }

    public void setExtendData(T extendData) {
        this.extendData = extendData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }
}
