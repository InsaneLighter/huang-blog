package com.huang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.ContentEntity;
import com.huang.entity.param.ContentParam;
import com.huang.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
public interface ContentService extends IService<ContentEntity> {
    PageUtils queryPage(Map<String, Object> params);

    void saveArticle(ContentParam contentParam);

    void updateArticle(ContentParam contentParam);
}

