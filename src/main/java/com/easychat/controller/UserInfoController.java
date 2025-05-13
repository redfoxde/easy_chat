package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.UserInfo;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.service.UserInfoService;
import com.easychat.utils.CopyTools;
import com.easychat.utils.StringTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.IOException;


/**
 * @ClassName UserInfoController
 * @Author chenhongxin
 * @Date 2025/5/13 下午12:57
 * @mood happy
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends ABaseController {
    @Resource
    private UserInfoService userInfoService;

    /**
     *获得用户信息
     */
    @RequestMapping("/getUserInfo")
    @GlobalInterceptor
    public ResponseVO getUserInfo(HttpServletRequest request) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(tokenUserInfoDto.getUserId());
        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
        userInfoVO.setAdmin(tokenUserInfoDto.getIsAdmin());
        return getSuccessResponseVO(userInfoVO);
    }

    /**
     *保存用户信息
     */
    @RequestMapping("/saveUserInfo")
    @GlobalInterceptor
    public ResponseVO saveUserInfo(HttpServletRequest request, UserInfo userInfo,
                                   MultipartFile avatarFile,
                                   MultipartFile avatarCover) throws IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
        userInfo.setUserId(tokenUserInfoDto.getUserId());
        userInfo.setPassword(null);
        userInfo.setStatus(null);
        userInfo.setCreatTime(null);
        userInfo.setLastLoginTime(null);

        this.userInfoService.updateUserInfo(userInfo, avatarFile, avatarCover);
        return getUserInfo(request);
    }
    /**
     *修改用户密码
     */
    @RequestMapping("/updatePassWord")
    @GlobalInterceptor
    public ResponseVO updatePassWord(HttpServletRequest request,
                                    @NotEmpty @Pattern(regexp = constants.REGEX_PASSWORD) String password){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(StringTools.encodeMd5(password));
        this.userInfoService.updateUserInfoByUserId(userInfo, tokenUserInfoDto.getUserId());

        //TODO 强制退出，重新登录

        return getSuccessResponseVO(0);
    }
    /**
     *退出登录
     */
    @RequestMapping("/logout")
    @GlobalInterceptor
    public ResponseVO logout(HttpServletRequest request){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto(request);
        //TODO 退出登录，关闭ws连接

        return getSuccessResponseVO(0);
    }

}
