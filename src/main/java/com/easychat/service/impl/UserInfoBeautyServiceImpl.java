package com.easychat.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.easychat.entity.enums.PageSize;
import com.easychat.entity.query.SimplePage;
import org.springframework.stereotype.Service;

import com.easychat.entity.query.UserInfoBeautyQuery;
import com.easychat.entity.po.UserInfoBeauty;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.mappers.UserInfoBeautyMapper;
import com.easychat.service.UserInfoBeautyService;
import com.easychat.utils.StringTools;


/**
 * 靓号表 业务接口实现
 */
@Service("userInfoBeautyService")
public class UserInfoBeautyServiceImpl implements UserInfoBeautyService {

	@Resource
	private UserInfoBeautyMapper<UserInfoBeauty, UserInfoBeautyQuery> userInfoBeautyMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery param) {
		return this.userInfoBeautyMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserInfoBeautyQuery param) {
		return this.userInfoBeautyMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserInfoBeauty> findListByPage(UserInfoBeautyQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserInfoBeauty> list = this.findListByParam(param);
		PaginationResultVO<UserInfoBeauty> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserInfoBeauty bean) {
		return this.userInfoBeautyMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfoBeauty> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoBeautyMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfoBeauty> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoBeautyMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserInfoBeauty bean, UserInfoBeautyQuery param) {
		StringTools.checkParam(param);
		return this.userInfoBeautyMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserInfoBeautyQuery param) {
		StringTools.checkParam(param);
		return this.userInfoBeautyMapper.deleteByParam(param);
	}

	/**
	 * 根据IdAndUserId获取对象
	 */
	@Override
	public UserInfoBeauty getUserInfoBeautyByIdAndUserId(Integer id, String userId) {
		return this.userInfoBeautyMapper.selectByIdAndUserId(id, userId);
	}

	/**
	 * 根据IdAndUserId修改
	 */
	@Override
	public Integer updateUserInfoBeautyByIdAndUserId(UserInfoBeauty bean, Integer id, String userId) {
		return this.userInfoBeautyMapper.updateByIdAndUserId(bean, id, userId);
	}

	/**
	 * 根据IdAndUserId删除
	 */
	@Override
	public Integer deleteUserInfoBeautyByIdAndUserId(Integer id, String userId) {
		return this.userInfoBeautyMapper.deleteByIdAndUserId(id, userId);
	}

	/**
	 * 根据UserId获取对象
	 */
	@Override
	public UserInfoBeauty getUserInfoBeautyByUserId(String userId) {
		return this.userInfoBeautyMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId修改
	 */
	@Override
	public Integer updateUserInfoBeautyByUserId(UserInfoBeauty bean, String userId) {
		return this.userInfoBeautyMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteUserInfoBeautyByUserId(String userId) {
		return this.userInfoBeautyMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Email获取对象
	 */
	@Override
	public UserInfoBeauty getUserInfoBeautyByEmail(String email) {
		return this.userInfoBeautyMapper.selectByEmail(email);
	}

	/**
	 * 根据Email修改
	 */
	@Override
	public Integer updateUserInfoBeautyByEmail(UserInfoBeauty bean, String email) {
		return this.userInfoBeautyMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public Integer deleteUserInfoBeautyByEmail(String email) {
		return this.userInfoBeautyMapper.deleteByEmail(email);
	}
}