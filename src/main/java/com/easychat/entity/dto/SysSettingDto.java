package com.easychat.entity.dto;


import com.easychat.entity.constants.constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @ClassName SysSettingDto
 * @Author chenhongxin
 * @Date 2025/5/7 下午3:54
 * @mood happy
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingDto implements Serializable {
    private static final long serialVersionUID = 1L;
    //最大群组数
    private Integer maxGroupCount = 5;
    private Integer minGroupMemberCount = 500;
    private Integer maxImageSize =2;
    private Integer minVideoSize =5;
    private Integer maxFileSize =5;
    private String robotUid = constants.ROBOT_UID;
    private String robotNickName = "Easy Chat";
    private String robotWelcome = "Easy Chat";

    public Integer getMaxGroupCount() {
        return maxGroupCount;
    }
    public void setMaxGroupCount(Integer maxGroupCount) {
        this.maxGroupCount = maxGroupCount;
    }
    public Integer getMinGroupMemberCount() {
        return minGroupMemberCount;
    }
    public void setMinGroupMemberCount(Integer minGroupMemberCount) {
        this.minGroupMemberCount = minGroupMemberCount;
    }
    public Integer getMaxImageSize() {
        return maxImageSize;
    }
    public void setMaxImageSize(Integer maxImageSize) {
        this.maxImageSize = maxImageSize;
    }
    public Integer getMinVideoSize() {
        return minVideoSize;
    }
    public void setMinVideoSize(Integer minVideoSize) {
        this.minVideoSize = minVideoSize;
    }
    public Integer getMaxFileSize() {
        return maxFileSize;
    }
    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    public String getRobotUid() {
        return robotUid;
    }
    public void setRobotUid(String robotUid) {
        this.robotUid = robotUid;
    }
    public String getRobotNickName() {
        return robotNickName;
    }
    public void setRobotNickName(String robotNickName) {
        this.robotNickName = robotNickName;
    }
    public String getRobotWelcome() {
        return robotWelcome;
    }
    public void setRobotWelcome(String robotWelcome) {
        this.robotWelcome = robotWelcome;
    }
}
