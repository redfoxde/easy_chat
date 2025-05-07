package com.easychat.test.entity.query;

import java.util.Date;


/**
 * 群组参数
 */
public class GroupInfoQuery extends BaseParam {


	/**
	 * 群ID
	 */
	private String groupId;

	private String groupIdFuzzy;

	/**
	 * 群组名
	 */
	private String groupName;

	private String groupNameFuzzy;

	/**
	 * 群主id
	 */
	private String groupOwerId;

	private String groupOwerIdFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 群公告
	 */
	private String groupNotice;

	private String groupNoticeFuzzy;

	/**
	 * 0：直接加入，1：管理员同意后加入
	 */
	private Integer joinType;

	/**
	 * 状态 1：正常 0：解散
	 */
	private Integer status;


	public void setGroupId(String groupId){
		this.groupId = groupId;
	}

	public String getGroupId(){
		return this.groupId;
	}

	public void setGroupIdFuzzy(String groupIdFuzzy){
		this.groupIdFuzzy = groupIdFuzzy;
	}

	public String getGroupIdFuzzy(){
		return this.groupIdFuzzy;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public String getGroupName(){
		return this.groupName;
	}

	public void setGroupNameFuzzy(String groupNameFuzzy){
		this.groupNameFuzzy = groupNameFuzzy;
	}

	public String getGroupNameFuzzy(){
		return this.groupNameFuzzy;
	}

	public void setGroupOwerId(String groupOwerId){
		this.groupOwerId = groupOwerId;
	}

	public String getGroupOwerId(){
		return this.groupOwerId;
	}

	public void setGroupOwerIdFuzzy(String groupOwerIdFuzzy){
		this.groupOwerIdFuzzy = groupOwerIdFuzzy;
	}

	public String getGroupOwerIdFuzzy(){
		return this.groupOwerIdFuzzy;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return this.createTime;
	}

	public void setCreateTimeStart(String createTimeStart){
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart(){
		return this.createTimeStart;
	}
	public void setCreateTimeEnd(String createTimeEnd){
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd(){
		return this.createTimeEnd;
	}

	public void setGroupNotice(String groupNotice){
		this.groupNotice = groupNotice;
	}

	public String getGroupNotice(){
		return this.groupNotice;
	}

	public void setGroupNoticeFuzzy(String groupNoticeFuzzy){
		this.groupNoticeFuzzy = groupNoticeFuzzy;
	}

	public String getGroupNoticeFuzzy(){
		return this.groupNoticeFuzzy;
	}

	public void setJoinType(Integer joinType){
		this.joinType = joinType;
	}

	public Integer getJoinType(){
		return this.joinType;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

}
