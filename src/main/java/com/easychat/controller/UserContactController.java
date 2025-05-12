package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.dto.UserContactSearchResultDto;
import com.easychat.entity.enums.PageSize;
import com.easychat.entity.enums.ResponseCodeEnum;
import com.easychat.entity.enums.UserContactStatusEnum;
import com.easychat.entity.enums.UserContactTypeEnum;
import com.easychat.entity.po.UserContact;
import com.easychat.entity.query.UserContactApplyQuery;
import com.easychat.entity.query.UserContactQuery;
import com.easychat.entity.vo.PaginationResultVO;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.exception.BusinessException;
import com.easychat.service.UserContactApplyService;
import com.easychat.service.UserContactService;
import com.easychat.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    /**
     *
     * 加载申请
     */

    @RequestMapping("/loadApply")
    @GlobalInterceptor
    public ResponseVO loadApply(HttpServletRequest request,Integer pageNo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);

        UserContactApplyQuery applyQuery = new UserContactApplyQuery();
        applyQuery.setOrderBy("last_apply_time desc");
        applyQuery.setReceiveUserId(tokenUserInfoDto.getUserId());
        applyQuery.setPageNo(pageNo);
        applyQuery.setPageSize(PageSize.SIZE15.getSize());
        PaginationResultVO resultVO =userContactApplyService.findListByPage(applyQuery);


        return getSuccessResponseVO(resultVO);

    }

    /**
     * 处理申请
     */
    @RequestMapping("/dealWithApply")
    @GlobalInterceptor
    public ResponseVO dealWithApply(HttpServletRequest request, @NotNull Integer applyId, @NotNull Integer status) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);

        this.userContactApplyService.dealWithApply(tokenUserInfoDto.getUserId(),applyId,status);

        return getSuccessResponseVO(0);

    }
    /**
     * 加载联系人
     */
    @RequestMapping("/loadContact")
    @GlobalInterceptor
    public ResponseVO loadContact(HttpServletRequest request, @NotNull String contactType) {

        UserContactTypeEnum contactTypeEnum = UserContactTypeEnum.getByName(contactType);

        if(null == contactTypeEnum) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
        UserContactQuery contactQuery = new UserContactQuery();
        contactQuery.setUserId(tokenUserInfoDto.getUserId());
        contactQuery.setContactType(contactTypeEnum.getType());
        //查用户
        if(contactTypeEnum.USER == contactTypeEnum) {
            contactQuery.setQueryContactUserInfo(true);
        }else if(contactTypeEnum.GROUP == contactTypeEnum) {
            contactQuery.setQueryGroupInfo(true);
            contactQuery.setExcludeMyGroup(true);
        }
        contactQuery.setOrderBy("last_update_time desc");
        contactQuery.setStatusArray(new Integer[]{
                UserContactStatusEnum.FRIEND.getStatus(),
                UserContactStatusEnum.DEL_BE.getStatus(),
                UserContactStatusEnum.BLACKLIST_BE.getStatus()
        });
        List<UserContact> contactList = userContactService.findListByParam(contactQuery);
        return getSuccessResponseVO(0);

    }

}
