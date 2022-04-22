package com.huang.config;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.huang.properties.OssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Time 2022-04-22 12:07
 * Created by Huang
 * className: OssConfig
 * Description:
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssConfig {
    @Autowired
    private OssProperties ossProperties;

    /**
     * 获取OSSClient
     */
    @Bean
    public OSSClient minioClient() {
        return new OSSClient(ossProperties.getEndpoint(),
                new DefaultCredentialProvider(ossProperties.getAccessKey(), ossProperties.getSecretKey()),
                new ClientConfiguration());
    }
}
