package com.huang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.CommentEntity;
import com.huang.utils.PageUtils;

import java.util.Map;

/**
 * @Time 2023-01-30 14:10
 * Created by Huang
 * className: CommentService
 * Description:
 */
public interface CommentService  extends IService<CommentEntity> {
    PageUtils queryPage(Map<String, Object> params);
}
