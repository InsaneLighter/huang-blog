package com.huang.config;

import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot 2.0 解决跨域问题
 *
 * @Author qinfeng
 *
 */
@Configuration
public class WebMvcConfiguration  {

    /**
     * actuator - Httptrace
     */
    @Bean
    public InMemoryHttpTraceRepository getInMemoryHttpTrace(){
        return new InMemoryHttpTraceRepository();
    }

}
