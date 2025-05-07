package com.easychat.controller;


import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.test.service.GroupInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 群组 Controller
 */
@RestController("groupInfoController")
@RequestMapping("/groupInfo")
public class GroupInfoController extends ABaseController {

	@Resource
	private GroupInfoService groupInfoService;

	@RequestMapping("/saveGroup")
	public ResponseVO saveGroup(HttpServletRequest request, String groupId,
								@NotEmpty String groupName,
								String groupNotice,
								@NotNull Integer joinType,
								MultipartFile avatarFile,
								MultipartFile avatarCover) {

		TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);

		return getSuccessResponseVO(null);
	}
}