package com.chang.common.Config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
public class ServiceInfo implements ApplicationListener<WebServerInitializedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ServiceInfo.class);
    @Value("${spring.application.name:app-default}")
    private String appName;
    @Value("${iline.platform.log.project.name:project-default }")
    private String projectName;
    @Value("${iline.platform.log.open.debug:true}")
    private boolean debugOn;
    @Value("${iline.platform.log.open.info:true}")
    private boolean infoOn;
    @Value("${iline.platform.log.open.warn:true}")
    private boolean warnOn;
    @Value("${iline.platform.log.open.error:true}")
    private boolean errorOn;
    @Value("${iline.platform.log.logfun.endable:true}")
    private Boolean endable;
    @Value("${server.port:0}")
    private int serverPort;
    private String hostIp;

    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    public int getPort() {
        return this.serverPort;
    }

    public String getHostIp() {
        try {
            this.hostIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get server host Exception e:", e);
        }

        return this.hostIp;
    }

    public static Logger getLogger() {
        return logger;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public boolean isDebugOn() {
        return this.debugOn;
    }

    public boolean isInfoOn() {
        return this.infoOn;
    }

    public boolean isWarnOn() {
        return this.warnOn;
    }

    public boolean isErrorOn() {
        return this.errorOn;
    }

    public Boolean getEndable() {
        return this.endable;
    }
}
