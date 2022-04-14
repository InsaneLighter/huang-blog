package com.huang;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @Time 2022-04-14 17:08
 * Created by Huang
 * className: Swagger2Configuration
 * Description:
 */
@EnableSwagger2WebMvc
@ConditionalOnProperty(prefix = "knife4j", name = "enable", havingValue = Swagger2Configuration.TRUE, matchIfMissing = true)
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Configuration {
    public static final String TRUE = "true";

    @Bean
    @ConditionalOnClass(SwaggerWebMvcConfigurer.class)
    public SwaggerWebMvcConfigurer getSwaggerWebMvcConfigurer() {
        return new SwaggerWebMvcConfigurer();
    }
}
