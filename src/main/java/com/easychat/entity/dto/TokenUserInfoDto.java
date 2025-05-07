package com.easychat.entity.dto;

import java.io.Serializable;

/**
 * @ClassName TokenUserInfoDto
 * @Author chenhongxin
 * @Date 2025/5/7 上午9:34
 * @mood happy
 */
public class TokenUserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private String userId;
    private String nickName;
    private Boolean isAdmin;


    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public Boolean getIsAdmin() {
        return isAdmin;
    }
}
