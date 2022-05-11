package com.huang.config;

import com.huang.properties.MinioProperties;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Time 2022-04-22 11:52
 * Created by Huang
 * className: MinioConfig
 * Description:
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {
    @Autowired
    private MinioProperties minioProperties;

    /**
     * 获取MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

}