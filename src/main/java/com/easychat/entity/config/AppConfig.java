package com.easychat.entity.config;

import com.easychat.utils.StringTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName AppConfig
 * @Author chenhongxin
 * @Date 2025/5/7 下午1:27
 * @mood happy
 */

@Configuration("appConfig")
public class AppConfig {
    /**
     * webSocket端口
     */
    @Value("${ws.port:}")
    private Integer wsPort;
    /**
     * 文件目录
     */
    @Value("${project.folder}")
    private String projectFolder;
    /**
     * 管理员
     */
    @Value("${admin.emails}")
    private String adminEmails;

    public Integer getWsPort() {
        return wsPort;
    }
    public String getProjectFolder() {
        if(StringTools.isEmpty(projectFolder)&&!projectFolder.endsWith("/")){
            projectFolder = projectFolder+"/";
        }
        return projectFolder;
    }
    public String getAdminEmails() {
        return adminEmails;
    }

}
