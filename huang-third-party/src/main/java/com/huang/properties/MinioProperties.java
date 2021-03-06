package com.huang.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Time 2022-04-22 11:53
 * Created by Huang
 * className: MinioProperties
 * Description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    /**
     * 连接地址
     */
    private String endpoint;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 域名
     */
    private String nginxHost;
}