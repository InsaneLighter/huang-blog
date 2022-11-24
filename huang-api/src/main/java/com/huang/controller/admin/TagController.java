package com.huang.controller.admin;

import com.huang.entity.TagEntity;
import com.huang.event.BlogEvent;
import com.huang.service.TagService;
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
@RequestMapping("/admin/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/queryAll")
    public R queryAll(){
        PageUtils page = tagService.queryAll();
        return R.ok().put("data", page);
    }

    @PostMapping
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping("/queryByIds")
    public R queryByIds(@RequestBody Map<String, Object> params){
        return tagService.queryByIds(params);
    }

    @GetMapping("/queryByName/{name}")
    public R queryByName(@PathVariable String name){
        TagEntity tagEntity = tagService.queryByName(name);
        return R.ok().put("tag",tagEntity);
    }

    @PostMapping
    public R save(@RequestBody TagEntity tag){
		tagService.saveTag(tag);
        applicationContext.publishEvent(new BlogEvent(this,tag));
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody TagEntity tag){
		tagService.updateById(tag);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		tagService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
