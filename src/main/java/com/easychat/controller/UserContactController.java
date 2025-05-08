package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.dto.UserContactSearchResultDto;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.service.UserContactApplyService;
import com.easychat.service.UserContactService;
import com.easychat.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @ClassName UserContactController
 * @Author chenhongxin
 * @Date 2025/5/8 下午2:38
 * @mood happy
 */
@Controller
@RequestMapping("/userContact")
public class UserContactController extends ABaseController {

    @Resource
    private UserContactService userContactService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserContactApplyService userContactApplyService;

    @RequestMapping("/search")
    @GlobalInterceptor
    public ResponseVO search(HttpServletRequest request,
                             @NotEmpty String contactId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
        UserContactSearchResultDto resultDto = userContactService.searchContact(tokenUserInfoDto.getUserId(), contactId);

        return getSuccessResponseVO(resultDto);

    }

    @RequestMapping("/applyAdd")
    @GlobalInterceptor
    public ResponseVO applyAdd(HttpServletRequest request,
                             @NotEmpty String contactId,
                               String applyInfo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
        Integer joinType = userContactService.applyAdd(tokenUserInfoDto,contactId,applyInfo);

        return getSuccessResponseVO(joinType);

    }
}
