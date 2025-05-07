package com.easychat.test.service;

import java.util.List;

import com.easychat.test.entity.query.GroupInfoQuery;
import com.easychat.test.entity.po.GroupInfo;
import com.easychat.test.entity.vo.PaginationResultVO;


/**
 * 群组 业务接口
 */
public interface GroupInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<GroupInfo> findListByParam(GroupInfoQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(GroupInfoQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<GroupInfo> findListByPage(GroupInfoQuery param);

	/**
	 * 新增
	 */
	Integer add(GroupInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<GroupInfo> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<GroupInfo> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(GroupInfo bean,GroupInfoQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(GroupInfoQuery param);

	/**
	 * 根据GroupId查询对象
	 */
	GroupInfo getGroupInfoByGroupId(String groupId);


	/**
	 * 根据GroupId修改
	 */
	Integer updateGroupInfoByGroupId(GroupInfo bean,String groupId);


	/**
	 * 根据GroupId删除
	 */
	Integer deleteGroupInfoByGroupId(String groupId);

}