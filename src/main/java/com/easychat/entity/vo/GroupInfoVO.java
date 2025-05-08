package com.easychat.entity.vo;

import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.po.UserInfo;

import java.util.List;

/**
 * @ClassName GroupInfoVO
 * @Author chenhongxin
 * @Date 2025/5/8 下午2:08
 * @mood happy
 */
public class GroupInfoVO {

    private GroupInfo groupInfo;
    private List<UserContact> userContactList;
    public List<UserContact> getUserContactList() {
        return userContactList;
    }
    public void setUserContactList(List<UserContact> userContactList) {
        this.userContactList = userContactList;
    }
    public GroupInfo getGroupInfo() {
        return groupInfo;
    }
    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

}
