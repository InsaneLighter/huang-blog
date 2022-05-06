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
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryAll(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/queryByIds")
    public R queryByIds(@RequestParam Map<String, Object> params){
        return categoryService.queryByIds(params);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		CategoryEntity category = categoryService.getById(id);
        return R.ok().put("category", category);
    }

    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        applicationContext.publishEvent(new BlogEvent(this,category));
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		categoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
