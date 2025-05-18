package com.easychat.entity.dto;

import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.po.ChatSessionUser;

import java.util.List;

/**
 * @ClassName WsInitData
 * @Author chenhongxin
 * @Date 2025/5/15 上午11:56
 * @mood happy
 */
public class WsInitData {
    private List<ChatSessionUser> chatSessionList;

    private List<ChatMessage> chatMessagesList;

    private Integer applyCount;

    public List<ChatSessionUser> getChatSessionList() {
        return chatSessionList;
    }

    public void setChatSessionList(List<ChatSessionUser> chatSessionList) {
        this.chatSessionList = chatSessionList;
    }

    public List<ChatMessage> getChatMessagesList() {
        return chatMessagesList;
    }

    public void setChatMessagesList(List<ChatMessage> chatMessagesList) {
        this.chatMessagesList = chatMessagesList;
    }

    public Integer getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }
}
