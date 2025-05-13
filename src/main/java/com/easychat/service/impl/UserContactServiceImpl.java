package com.easychat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.dto.UserContactSearchResultDto;
import com.easychat.entity.enums.*;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.po.UserContactApply;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.query.*;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.GroupInfoMapper;
import com.easychat.mappers.UserContactApplyMapper;
import com.easychat.mappers.UserInfoMapper;
import com.easychat.redis.RedisComponent;
import com.easychat.service.UserContactApplyService;
import com.easychat.utils.CopyTools;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import com.easychat.entity.po.UserContact;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.mappers.UserContactMapper;
import com.easychat.service.UserContactService;
import com.easychat.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 联系人 业务接口实现
 */
@Service("userContactService")
public class UserContactServiceImpl implements UserContactService {

	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

	@Resource
	private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

	@Resource
	private RedisComponent redisComponent;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserContact> findListByParam(UserContactQuery param) {
		return this.userContactMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserContactQuery param) {
		return this.userContactMapper.selectCount(param);
	}


	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserContact> findListByPage(UserContactQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserContact> list = this.findListByParam(param);
		PaginationResultVO<UserContact> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserContact bean) {
		return this.userContactMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserContact> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userContactMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserContact> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userContactMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserContact bean, UserContactQuery param) {
		StringTools.checkParam(param);
		return this.userContactMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserContactQuery param) {
		StringTools.checkParam(param);
		return this.userContactMapper.deleteByParam(param);
	}

	/**
	 * 根据UserIdAndContactId获取对象
	 */
	@Override
	public UserContact getUserContactByUserIdAndContactId(String userId, String contactId) {
		return this.userContactMapper.selectByUserIdAndContactId(userId, contactId);
	}

	/**
	 * 根据UserIdAndContactId修改
	 */
	@Override
	public Integer updateUserContactByUserIdAndContactId(UserContact bean, String userId, String contactId) {
		return this.userContactMapper.updateByUserIdAndContactId(bean, userId, contactId);
	}

	/**
	 * 根据UserIdAndContactId删除
	 */
	@Override
	public Integer deleteUserContactByUserIdAndContactId(String userId, String contactId) {
		return this.userContactMapper.deleteByUserIdAndContactId(userId, contactId);
	}

	/**
	 * 搜索好友
	 */
	@Override
	public UserContactSearchResultDto searchContact(String userId, String contactId) {
		//搜索类型
		UserContactTypeEnum typeEnum = UserContactTypeEnum.getByPrefix(contactId);
		if (typeEnum == null) {
			return null;
		}
		UserContactSearchResultDto resultDto = new UserContactSearchResultDto();
		switch (typeEnum) {
			case USER:
				UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
				if (userInfo == null) {
					return null;
				}
				resultDto = CopyTools.copy(userInfo, UserContactSearchResultDto.class);

				break;
			case GROUP:
				GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
				if (groupInfo == null) {
					return null;
				}
				resultDto.setNickName(groupInfo.getGroupName());
				break;
		}

		resultDto.setContactType(typeEnum.toString());
		resultDto.setContactId(contactId);

		if(userId.equals(contactId)){
			resultDto.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			return resultDto;
		}
		//查询是否是好友
		UserContact userContact = userContactMapper.selectByUserIdAndContactId(userId, contactId);
		resultDto.setStatus(userContact==null?null:userContact.getStatus());


        return resultDto;
    }

	/**
	 * 添加好友
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer applyAdd(TokenUserInfoDto tokenUserInfoDto,String contactId,String applyInfo) {
		UserContactTypeEnum typeEnum = UserContactTypeEnum.getByPrefix(contactId);
		if (typeEnum == null) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//申请人信息
		String applyUserId = tokenUserInfoDto.getUserId();

		//默认信息
		applyInfo = StringTools.isEmpty(applyInfo)?String.format(constants.APPLY_INFO_TEMPLATE,tokenUserInfoDto.getNickName()): applyInfo;

		Long curTime = System.currentTimeMillis();

		Integer joinType = null;
		String receiveUserId = contactId;

		//查询对方好友是否添加，如果已拉黑则无法添加
		UserContact userContact = userContactMapper.selectByUserIdAndContactId(applyUserId, contactId);
		if (userContact != null&&
				ArrayUtils.contains(new Integer[]{
						UserContactStatusEnum.BLACKLIST_BE.getStatus(),
						UserContactStatusEnum.BLACKLIST_BE_FIRST.getStatus(),
				},userContact.getStatus())
				) {
			throw new BusinessException("对方已将你拉黑");
		}

		//加群
		if(UserContactTypeEnum.GROUP.equals(typeEnum)){
			GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
			if (groupInfo == null|| GroupStatusEnum.DISSOLUTION.getStatus().equals(groupInfo.getStatus())) {
				throw new BusinessException("群聊不存在或已解散");
			}
			receiveUserId = groupInfo.getGroupId();
			joinType = groupInfo.getJoinType();
		}else {
			UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
			if (userInfo == null) {
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			joinType = userInfo.getJoinType();
		}

		//直接加入不用申请记录
		if(JoinTypeEnum.JOIN.getType().equals(joinType)){
			this.addContact(applyUserId,receiveUserId,contactId,typeEnum.getType(),applyInfo);

			return joinType;
		}

		UserContactApply dbApply = this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
		if(dbApply == null){
			UserContactApply contactApply = new UserContactApply();
			contactApply.setApplyUserId(applyUserId);
			contactApply.setContactType(typeEnum.getType());
			contactApply.setReceiveUserId(receiveUserId);
			contactApply.setLastApplyTime(curTime);
			contactApply.setContactId(contactId);
			contactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
			contactApply.setApplyInfo(applyInfo);
			this.userContactApplyMapper.insert(contactApply);
		}else{
			//更新状态
			UserContactApply contactApply = new UserContactApply();
			contactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
			contactApply.setApplyUserId(applyUserId);
			contactApply.setApplyInfo(applyInfo);
			this.userContactApplyMapper.updateByApplyId(contactApply,dbApply.getApplyId());
		}
		if(dbApply == null||!dbApply.getStatus().equals(UserContactApplyStatusEnum.INIT.getStatus())){
			//TODO 发送ws消息
		}

        return joinType;
    }

	@Override
	@Transactional(rollbackFor = BusinessException.class)
	public void addContact(String applyUserId, String receiveUserId, String contactId, Integer contactType, String applyInfo) {
		//群聊人数
		if(UserContactTypeEnum.GROUP.getType().equals(contactType)) {
			UserContactQuery userContactQuery = new UserContactQuery();
			userContactQuery.setUserId(contactId);
			userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());

			Integer count = userContactMapper.selectCount(userContactQuery);
			SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
			if(count>=sysSettingDto.getMaxGroupCount()) {
				throw new BusinessException("成员已满，无法加入");
			}
		}
		Date curDate = new Date();
		//同意，双方添加好友
		List<UserContact> contactList = new ArrayList<UserContact>();
		//申请人添加对方
		UserContact userContact = new UserContact();
		userContact.setUserId(applyUserId);
		userContact.setContactId(contactId);
		userContact.setContactType(contactType);
		userContact.setCreateTime(curDate);
		userContact.setLastUpdateTime(curDate);
		userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
		contactList.add(userContact);

		//申请好友接收人添加申请人，群组不需要添加对方为好友
		if(UserContactTypeEnum.USER.getType().equals(contactType)) {
			userContact = new UserContact();
			userContact.setUserId(receiveUserId);
			userContact.setContactId(applyUserId);
			userContact.setContactType(contactType);
			userContact.setCreateTime(curDate);
			userContact.setLastUpdateTime(curDate);
			userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			contactList.add(userContact);
		}
		//批量插入
		userContactMapper.insertOrUpdate(userContact);
		//TODO 如果是好友，接收人也添加申请人为好友 添加缓存

		//TODO 创建会话 发送消息
	}

	@Override
	public void removeUserContact(String userId, String contactId, UserContactStatusEnum statusEnum) {
		//移除好友
		UserContact userContact = new UserContact();
		userContact.setStatus(statusEnum.getStatus());
		userContactMapper.updateByUserIdAndContactId(userContact,userId,contactId);
		//在好友列表中移除自己
		UserContact friendContact = new UserContact();
		if(UserContactStatusEnum.DEL == statusEnum){
			friendContact.setStatus(UserContactStatusEnum.DEL_BE.getStatus());
		}else if(UserContactStatusEnum.BLACKLIST == statusEnum){
			friendContact.setStatus(UserContactStatusEnum.BLACKLIST_BE.getStatus());
		}
		userContactMapper.updateByUserIdAndContactId(friendContact,contactId,userId);
		//TODO 从我的好友列表中删除好友
		//TODO 从好友列表缓存中删除我
	}
}