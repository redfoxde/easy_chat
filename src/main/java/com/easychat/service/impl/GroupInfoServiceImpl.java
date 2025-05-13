package com.easychat.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;


import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.enums.*;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.entity.query.UserInfoQuery;
import com.easychat.exception.BusinessException;
import com.easychat.mappers.UserContactMapper;
import com.easychat.redis.RedisComponent;
import org.springframework.stereotype.Service;

import com.easychat.entity.query.GroupInfoQuery;
import com.easychat.entity.po.GroupInfo;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.query.SimplePage;
import com.easychat.mappers.GroupInfoMapper;
import com.easychat.service.GroupInfoService;
import com.easychat.utils.StringTools;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * 群组 业务接口实现
 */
@Service("groupInfoService")
public class GroupInfoServiceImpl implements GroupInfoService {

	@Resource
	private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

	@Resource
	public RedisComponent redisComponent;

	@Resource
	private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

	@Resource
	private AppConfig appConfig;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<GroupInfo> findListByParam(GroupInfoQuery param) {
		return this.groupInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(GroupInfoQuery param) {
		return this.groupInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<GroupInfo> list = this.findListByParam(param);
		PaginationResultVO<GroupInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(GroupInfo bean) {
		return this.groupInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<GroupInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.groupInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<GroupInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.groupInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(GroupInfo bean, GroupInfoQuery param) {
		StringTools.checkParam(param);
		return this.groupInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(GroupInfoQuery param) {
		StringTools.checkParam(param);
		return this.groupInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据GroupId获取对象
	 */
	@Override
	public GroupInfo getGroupInfoByGroupId(String groupId) {
		return this.groupInfoMapper.selectByGroupId(groupId);
	}

	/**
	 * 根据GroupId修改
	 */
	@Override
	public Integer updateGroupInfoByGroupId(GroupInfo bean, String groupId) {
		return this.groupInfoMapper.updateByGroupId(bean, groupId);
	}

	/**
	 * 根据GroupId删除
	 */
	@Override
	public Integer deleteGroupInfoByGroupId(String groupId) {
		return this.groupInfoMapper.deleteByGroupId(groupId);
	}

	/**
	 * 创建群组
	 */
	@Override
	//事务
	@Transactional(rollbackFor = Exception.class)
	public void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
		Date curDate = new Date();
		//新增
		if(StringTools.isEmpty(groupInfo.getGroupId())){
			GroupInfoQuery groupInfoQuery = new GroupInfoQuery();
			groupInfoQuery.setGroupOwnerId(groupInfo.getGroupOwnerId());
			//统计创建的群聊数目
			Integer count = this.groupInfoMapper.selectCount(groupInfoQuery);
			SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
			if (count>sysSettingDto.getMaxGroupCount()){
				throw new BusinessException("最多只能创建"+sysSettingDto.getMaxGroupCount()+"个群聊");
			}
			if (null == avatarFile){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			groupInfo.setCreateTime(curDate);
			groupInfo.setGroupId(StringTools.getGroupId());
			this.groupInfoMapper.insert(groupInfo);

			//将群组添加为联系人
			UserContact userContact = new UserContact();
			userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
			userContact.setContactType(UserContactTypeEnum.GROUP.getType());
			userContact.setContactId(groupInfo.getGroupId());
			userContact.setUserId(groupInfo.getGroupOwnerId());
			userContact.setCreateTime(curDate);
			userContact.setLastUpdateTime(curDate);
			this.userContactMapper.insert(userContact);

			//TODO 创建会话
			//TODO 发送消息

		}else{
			//修改
			GroupInfo dbGroupInfo = this.groupInfoMapper.selectByGroupId(groupInfo.getGroupId());
			//避免绕过客户端进行修改
			if(!dbGroupInfo.getGroupOwnerId().equals(groupInfo.getGroupOwnerId())){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			this.groupInfoMapper.updateByGroupId(groupInfo,groupInfo.getGroupId());
			//TODO 更新相关表冗余信息

			//TODO 修改群昵称发送WS昵称
		}
		//头像
		if(null == avatarFile){
			return;
		}
		String baseFolder = appConfig.getProjectFolder()+ constants.FILE_FOLDER_FILE;
		File targetFileFolder = new File(baseFolder+constants.FILE_FOLDER_AVATAR_NAME);
		if(!targetFileFolder.exists()){
			targetFileFolder.mkdirs();
		}
		String filePath = targetFileFolder+"/"+groupInfo.getGroupId()+constants.IMAGE_SUFFIX;
		avatarFile.transferTo(new File(filePath));
		avatarCover.transferTo(new File(filePath+constants.COVER_IMAGE_SUFFIX));

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dissolutionGroup(String groupOwnerId, String groupId) {
		GroupInfo dbInfo = this.groupInfoMapper.selectByGroupId(groupId);
		if(null==dbInfo || !dbInfo.getGroupOwnerId().equals(groupOwnerId)){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//删除群组
		GroupInfo updateInfo = new GroupInfo();
		updateInfo.setStatus(GroupStatusEnum.DISSOLUTION.getStatus());
		this.groupInfoMapper.updateByGroupId(updateInfo,groupId);

		//删除联系人
		UserContactQuery userContactQuery = new UserContactQuery();
		userContactQuery.setUserId(groupId);
		userContactQuery.setContactType(UserContactTypeEnum.GROUP.getType());

		UserContact updateContact = new UserContact();
		updateContact.setStatus(UserContactStatusEnum.DEL.getStatus());
		this.userContactMapper.updateByParam(updateContact,userContactQuery);

		//TODO 移除相关群员的联系人缓存
		//TODO 发消息： 1.更新会话信息 2.记录群信息 3.发送解散群通知信息

	}
}