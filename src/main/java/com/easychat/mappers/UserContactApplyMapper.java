package com.easychat.test.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 申请表 数据库操作接口
 */
public interface UserContactApplyMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据ApplyId更新
	 */
	 Integer updateByApplyId(@Param("bean") T t,@Param("applyId") Integer applyId);


	/**
	 * 根据ApplyId删除
	 */
	 Integer deleteByApplyId(@Param("applyId") Integer applyId);


	/**
	 * 根据ApplyId获取对象
	 */
	 T selectByApplyId(@Param("applyId") Integer applyId);


}
