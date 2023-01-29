package com.huang.controller.admin;

import com.huang.entity.TodoEntity;
import com.huang.service.TodoService;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-11-18 15:23:48
 */
@RestController
@RequestMapping("/admin/todo")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping("/queryAll")
    public R queryAll(@RequestBody Map<String, Object> params){
        List<TodoEntity> todoEntityList = todoService.queryAll(params);
        return R.ok().put("data", todoEntityList);
    }

    @GetMapping("/get/{id}")
    public R info(@PathVariable("id") Integer id){
		TodoEntity todo = todoService.getById(id);
        return R.ok().put("todo", todo);
    }

    @PostMapping
    public R save(@RequestBody TodoEntity todo){
		todoService.save(todo);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody TodoEntity todo){
		todoService.updateById(todo);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String[] ids){
		todoService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
