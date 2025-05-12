package com.easychat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.enums.*;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.UserContactMapper;
import com.easychat.redis.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easychat.entity.query.UserContactApplyQuery;
import com.easychat.entity.po.UserContactApply;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.query.SimplePage;
import com.easychat.mappers.UserContactApplyMapper;
import com.easychat.service.UserContactApplyService;
import com.easychat.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;


/**
 * 申请表 业务接口实现
 */
@Service("userContactApplyService")
public class UserContactApplyServiceImpl implements UserContactApplyService {

	@Resource
	private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Resource
    private RedisComponent redisComponent;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserContactApply> findListByParam(UserContactApplyQuery param) {
		return this.userContactApplyMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserContactApplyQuery param) {
		return this.userContactApplyMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserContactApply> list = this.findListByParam(param);
		PaginationResultVO<UserContactApply> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserContactApply bean) {
		return this.userContactApplyMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserContactApply> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userContactApplyMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserContactApply> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userContactApplyMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserContactApply bean, UserContactApplyQuery param) {
		StringTools.checkParam(param);
		return this.userContactApplyMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserContactApplyQuery param) {
		StringTools.checkParam(param);
		return this.userContactApplyMapper.deleteByParam(param);
	}

	/**
	 * 根据ApplyId获取对象
	 */
	@Override
	public UserContactApply getUserContactApplyByApplyId(Integer applyId) {
		return this.userContactApplyMapper.selectByApplyId(applyId);
	}

	/**
	 * 根据ApplyId修改
	 */
	@Override
	public Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId) {
		return this.userContactApplyMapper.updateByApplyId(bean, applyId);
	}

	/**
	 * 根据ApplyId删除
	 */
	@Override
	public Integer deleteUserContactApplyByApplyId(Integer applyId) {
		return this.userContactApplyMapper.deleteByApplyId(applyId);
	}

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId获取对象
	 */
	@Override
	public UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
		return this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
	}

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId修改
	 */
	@Override
	public Integer updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean, String applyUserId, String receiveUserId, String contactId) {
		return this.userContactApplyMapper.updateByApplyUserIdAndReceiveUserIdAndContactId(bean, applyUserId, receiveUserId, contactId);
	}

	/**
	 * 根据ApplyUserIdAndReceiveUserIdAndContactId删除
	 */
	@Override
	public Integer deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
		return this.userContactApplyMapper.deleteByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
	}

	@Override
	@Transactional(rollbackFor = BusinessException.class)
	public void dealWithApply(String userId, Integer applyId, Integer status) {
		UserContactApplyStatusEnum statusEnum = UserContactApplyStatusEnum.getByStatus(status);
		if (statusEnum == null||UserContactApplyStatusEnum.INIT ==statusEnum) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		UserContactApply applyInfo = this.userContactApplyMapper.selectByApplyId(applyId);
		if(applyInfo==null|| !userId.equals(applyInfo.getApplyUserId())) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		UserContactApply updateInfo = new UserContactApply();
		updateInfo.setStatus(statusEnum.getStatus());
		updateInfo.setLastApplyTime(System.currentTimeMillis());

		UserContactApplyQuery applyQuery = new UserContactApplyQuery();
		applyQuery.setApplyId(applyId);
		applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());

		Integer count = userContactApplyMapper.updateByParam(updateInfo, applyQuery);
		if(count==0) {
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//通过
		if(UserContactApplyStatusEnum.PASS.getStatus().equals(status)) {

			this.addContact(applyInfo.getApplyUserId(),applyInfo.getReceiveUserId(),applyInfo.getContactId(),applyInfo.getContactType(),applyInfo.getApplyInfo());

			return;
		}
		//拉黑
		if(UserContactApplyStatusEnum.BLACKLIST.getStatus().equals(status)){
			Date curDate = new Date();
			UserContact userContact = new UserContact();
			userContact.setUserId(applyInfo.getApplyUserId());
			userContact.setContactId(applyInfo.getContactId());
			userContact.setContactType(applyInfo.getContactType());
			userContact.setCreateTime(curDate);
			userContact.setStatus(UserContactApplyStatusEnum.BLACKLIST_BE.getStatus());
			userContact.setLastUpdateTime(curDate);
			userContactMapper.insertOrUpdate(userContact);

		}
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


}