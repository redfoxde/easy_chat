package com.easychat.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 靓号表 数据库操作接口
 */
public interface UserInfoBeautyMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据IdAndUserId更新
	 */
	 Integer updateByIdAndUserId(@Param("bean") T t,@Param("id") Integer id,@Param("userId") String userId);


	/**
	 * 根据IdAndUserId删除
	 */
	 Integer deleteByIdAndUserId(@Param("id") Integer id,@Param("userId") String userId);


	/**
	 * 根据IdAndUserId获取对象
	 */
	 T selectByIdAndUserId(@Param("id") Integer id,@Param("userId") String userId);


	/**
	 * 根据UserId更新
	 */
	 Integer updateByUserId(@Param("bean") T t,@Param("userId") String userId);


	/**
	 * 根据UserId删除
	 */
	 Integer deleteByUserId(@Param("userId") String userId);


	/**
	 * 根据UserId获取对象
	 */
	 T selectByUserId(@Param("userId") String userId);


	/**
	 * 根据Email更新
	 */
	 Integer updateByEmail(@Param("bean") T t,@Param("email") String email);


	/**
	 * 根据Email删除
	 */
	 Integer deleteByEmail(@Param("email") String email);


	/**
	 * 根据Email获取对象
	 */
	 T selectByEmail(@Param("email") String email);

	/**
	 *根据ID进行删除
	 */
	Integer deleteById(@Param("id") Integer id);
}
