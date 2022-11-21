package com.huang.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.TodoEntity;
import com.huang.mapper.TodoMapper;
import com.huang.service.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("todoService")
public class TodoServiceImpl extends ServiceImpl<TodoMapper, TodoEntity> implements TodoService {
    @Override
    public List<TodoEntity> queryAll(Map<String, Object> params) {
        QueryWrapper<TodoEntity> todoEntityQueryWrapper = new QueryWrapper<>();
        return this.list(todoEntityQueryWrapper);
    }

}