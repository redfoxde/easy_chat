package com.easychat.controller;

import java.util.List;

import com.easychat.entity.query.ChatMessageQuery;
import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.service.ChatMessageService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 聊天消息表 Controller
 */
@RestController("chatMessageController")
@RequestMapping("/chatMessage")
public class ChatMessageController extends ABaseController{

	@Resource
	private ChatMessageService chatMessageService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ChatMessageQuery query){
		return getSuccessResponseVO(chatMessageService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(ChatMessage bean) {
		chatMessageService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<ChatMessage> listBean) {
		chatMessageService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ChatMessage> listBean) {
		chatMessageService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据MessageId查询对象
	 */
	@RequestMapping("/getChatMessageByMessageId")
	public ResponseVO getChatMessageByMessageId(Long messageId) {
		return getSuccessResponseVO(chatMessageService.getChatMessageByMessageId(messageId));
	}

	/**
	 * 根据MessageId修改对象
	 */
	@RequestMapping("/updateChatMessageByMessageId")
	public ResponseVO updateChatMessageByMessageId(ChatMessage bean,Long messageId) {
		chatMessageService.updateChatMessageByMessageId(bean,messageId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据MessageId删除
	 */
	@RequestMapping("/deleteChatMessageByMessageId")
	public ResponseVO deleteChatMessageByMessageId(Long messageId) {
		chatMessageService.deleteChatMessageByMessageId(messageId);
		return getSuccessResponseVO(null);
	}
}