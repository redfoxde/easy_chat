package com.easychat.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName UserInfoVO
 * @Author chenhongxin
 * @Date 2025/5/7 下午2:36
 * @mood happy
 */
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 0.直接加入 1.同意后加好友
     */
    private Integer joinType;

    /**
     * 性别 0女 1男
     */
    private Integer sex;
    /**
     * 个性签名
     */
    private String personalSignature;
    /**
     * 地区
     */
    private String areaName;

    /**
     * 地区编号
     */
    private String areaCode;

    private String token;

    private Boolean admin;

    private Integer contactStatus;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public Integer getJoinType() {
        return joinType;
    }
    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }
    public Integer getSex(){
        return sex;
    }
    public void setSex(Integer sex){
        this.sex=sex;
    }
    public String getPersonalSignature() {
        return personalSignature;
    }
    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }
    public String getAreaName() {
        return areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public String getAreaCode() {
        return areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Boolean getAdmin() {
        return admin;
    }
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
    public Integer getContactStatus() {
        return contactStatus;
    }
    public void setContactStatus(Integer contactStatus) {
        this.contactStatus = contactStatus;
    }

}
