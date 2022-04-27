package com.huang.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @Time 2022-04-14 19:24
 * Created by Huang
 * className: MybatisConfiguration
 * Description:
 */
@Configuration
public class MybatisConfiguration implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        //TODO 暂时默认Huang  之后指定创建用户
        this.strictInsertFill(metaObject, "createBy", String.class, "Huang");
        //TODO 暂时默认Huang  之后指定修改用户
        this.strictInsertFill(metaObject, "updateBy", String.class, "Huang");
        this.strictInsertFill(metaObject, "version", Integer.class, 0);
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "likes", Integer.class, 0);
        this.strictInsertFill(metaObject, "visit", Integer.class, 0);
        this.strictInsertFill(metaObject, "topPriority", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        //TODO 暂时默认Huang  之后指定修改用户
        this.strictUpdateFill(metaObject, "updateBy", String.class, "Huang");
    }

    @Bean
    public IKeyGenerator mybatisKeyGenerator() {
        return new H2KeyGenerator();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

}
