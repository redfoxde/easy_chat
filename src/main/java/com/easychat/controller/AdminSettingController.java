package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.constants;
import com.easychat.entity.dto.SysSettingDto;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.redis.RedisComponent;
import com.easychat.service.GroupInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName AdminSettingController
 * @Author chenhongxin
 * @Date 2025/5/13 下午6:00
 * @mood happy
 */
@RestController("adminSettingController")
@RequestMapping("/admin")
public class AdminSettingController extends ABaseController {
    @Resource
    private GroupInfoService groupInfoService;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    /**
     *获取系统设置
     */
    @RequestMapping("/getSysSetting")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO getSysSetting() {
        SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
        return getSuccessResponseVO(sysSettingDto);
    }

    /**
     *保存设置
     */
    @RequestMapping("/saveSysSetting")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO saveSysSetting(SysSettingDto sysSettingDto,
                                     MultipartFile robotFile,
                                     MultipartFile robotCover) throws IOException {
        //上传头像
        if(robotFile != null) {
            String baseFolder = appConfig.getProjectFolder() + constants.FILE_FOLDER_FILE;
            File targetFileFolder = new File(baseFolder+constants.FILE_FOLDER_AVATAR_NAME);
            if(!targetFileFolder.exists()) {
                targetFileFolder.mkdirs();
            }
            String filePath = targetFileFolder.getPath()+"/"+constants.ROBOT_UID+constants.IMAGE_SUFFIX;
            robotFile.transferTo(new File(filePath));
            robotFile.transferTo(new File(filePath+constants.COVER_IMAGE_SUFFIX));

        }
        redisComponent.saveSysSetting(sysSettingDto);
        return getSuccessResponseVO(0);
    }

}
