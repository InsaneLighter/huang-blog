package com.huang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.PostEntity;
import com.huang.utils.PageUtils;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-12 18:28:21
 */
public interface PostService extends IService<PostEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

