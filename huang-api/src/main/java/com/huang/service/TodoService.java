package com.huang.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.utils.PageUtils;
import com.huang.entity.TodoEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-11-18 15:23:48
 */
public interface TodoService extends IService<TodoEntity> {
    List<TodoEntity> queryAll(Map<String, Object> params);
}

