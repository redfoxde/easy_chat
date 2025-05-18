package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.entity.vo.UserInfoVO;
import com.easychat.exception.BusinessException;
import com.easychat.redis.RedisComponent;
import com.easychat.redis.RedisUtils;
import com.easychat.service.UserInfoService;
import com.easychat.websocket.netty.MessageHandler;
import com.wf.captcha.ArithmeticCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName AccountController
 * @Author chenhongxin
 * @Date 2025/5/6 下午3:34
 * @mood happy
 */
@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private MessageHandler messageHandler;

    @RequestMapping("/checkCode")
    public ResponseVO checkCode() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100,42);
        String code = captcha.text();
        String checkCodeKey = UUID.randomUUID().toString();

        redisUtils.set(constants.EASY_CHAT+checkCodeKey, code, constants.Time_IMIN*10);
        String checkCodeBase64 = captcha.toBase64();
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("checkCodeBase64",checkCodeBase64);
        result.put("checkCodeKey",checkCodeKey);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/register")
    public ResponseVO register(@NotEmpty String checkCodeKey,
                               @NotEmpty @Email String email,
                               @NotEmpty @Pattern(regexp = constants.REGEX_PASSWORD) String password,
                               @NotEmpty String nickName,
                               @NotEmpty String checkCode) {
        try{
            if(!checkCode.equals((String) redisUtils.get(constants.EASY_CHAT+checkCodeKey))){
                throw new BusinessException("图片验证码不正确！");
            }
            userInfoService.register(email,nickName,password);
            return getSuccessResponseVO(null);

        }finally {
            redisUtils.delete(constants.EASY_CHAT+checkCodeKey);
        }

    }

    @RequestMapping("/login")
    public ResponseVO login(@NotEmpty String checkCodeKey,
                               @NotEmpty @Email String email,
                               @NotEmpty String password,
                               @NotEmpty String checkCode) {
        try{
            if(!checkCode.equals((String) redisUtils.get(constants.EASY_CHAT+checkCodeKey))){
                throw new BusinessException("图片验证码不正确！");
            }
            UserInfoVO userInfoVO= userInfoService.login(email,password);


            return getSuccessResponseVO(userInfoVO);

        }finally {
            redisUtils.delete(constants.EASY_CHAT+checkCodeKey);
        }

    }

    @GlobalInterceptor
    @RequestMapping("/getSysSetting")
    public ResponseVO login() {
        return getSuccessResponseVO(redisComponent.getSysSettingDto());
    }



    @RequestMapping("/test")
    public ResponseVO test(){
        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageContent("哈哈哈哈"+System.currentTimeMillis());

        messageHandler.sendMessage(messageSendDto);
        return getSuccessResponseVO(null);
    }
}

