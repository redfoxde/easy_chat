package com.easychat.service.impl;

import java.lang.annotation.Target;
import java.util.List;

import javax.annotation.Resource;

import com.easychat.entity.constants.constants;
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
import com.easychat.utils.CopyTools;
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
		if (userContact != null&& UserContactStatusEnum.BLACKLIST_BE.getStatus().equals(userContact.getStatus())) {
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
			//TODO 添加联系人

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
}