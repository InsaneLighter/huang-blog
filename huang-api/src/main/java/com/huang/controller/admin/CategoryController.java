package com.huang.controller.admin;

import com.huang.entity.CategoryEntity;
import com.huang.event.BlogEvent;
import com.huang.service.CategoryService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/queryAll")
    public R queryAll(){
        PageUtils page = categoryService.queryAll();
        return R.ok().put("data", page);
    }

    @GetMapping("/queryAllTree")
    public R queryAllTree(){
        PageUtils page = categoryService.queryAllTree();
        return R.ok().put("data", page);
    }

    @PostMapping("/queryByIds")
    public R queryByIds(@RequestBody Map<String, Object> params){
        Map<String,Object> map = categoryService.queryByIds(params);
        return R.ok(map);
    }

    @GetMapping("/queryByName/{name}")
    public R queryByName(@PathVariable String name){
        CategoryEntity categoryEntity = categoryService.queryByName(name);
        return R.ok().put("category",categoryEntity);
    }

    @PostMapping
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        applicationContext.publishEvent(new BlogEvent(this,category));
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		categoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
