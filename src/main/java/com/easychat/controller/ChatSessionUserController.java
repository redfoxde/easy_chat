package com.easychat.controller;

import java.util.List;

import com.easychat.entity.query.ChatSessionUserQuery;
import com.easychat.entity.po.ChatSessionUser;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.service.ChatSessionUserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 会话用户 Controller
 */
@RestController("chatSessionUserController")
@RequestMapping("/chatSessionUser")
public class ChatSessionUserController extends ABaseController{

	@Resource
	private ChatSessionUserService chatSessionUserService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ChatSessionUserQuery query){
		return getSuccessResponseVO(chatSessionUserService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(ChatSessionUser bean) {
		chatSessionUserService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ChatSessionUser> listBean) {
		chatSessionUserService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ChatSessionUser> listBean) {
		chatSessionUserService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserIdAndContactId查询对象
	 */
	@RequestMapping("/getChatSessionUserByUserIdAndContactId")
	public ResponseVO getChatSessionUserByUserIdAndContactId(String userId,String contactId) {
		return getSuccessResponseVO(chatSessionUserService.getChatSessionUserByUserIdAndContactId(userId,contactId));
	}

	/**
	 * 根据UserIdAndContactId修改对象
	 */
	@RequestMapping("/updateChatSessionUserByUserIdAndContactId")
	public ResponseVO updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean,String userId,String contactId) {
		chatSessionUserService.updateChatSessionUserByUserIdAndContactId(bean,userId,contactId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserIdAndContactId删除
	 */
	@RequestMapping("/deleteChatSessionUserByUserIdAndContactId")
	public ResponseVO deleteChatSessionUserByUserIdAndContactId(String userId,String contactId) {
		chatSessionUserService.deleteChatSessionUserByUserIdAndContactId(userId,contactId);
		return getSuccessResponseVO(null);
	}
}