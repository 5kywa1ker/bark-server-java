package cn.skywa1ker.bark.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 应用全局配置
 * 
 * @author hfb
 * @date 19/10/4
 */
@ConfigurationProperties(prefix = "application")
@Component
@Data
public class ApplicationConfigProperties {
    /**
     * 证书文件名
     */
    private String certFileName = "cert-20210125.p12";
    /**
     * 证书密码
     */
    private String certPwd = "bp";
}
