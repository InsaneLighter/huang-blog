package com.huang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Time 2022-04-12 14:25
 * Created by Huang
 * className: Application
 * Description:
 */
@SpringBootApplication
@MapperScan(basePackages = "com.huang.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
