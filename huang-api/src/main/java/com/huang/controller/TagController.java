package com.huang.controller;
import com.huang.entity.TagEntity;
import com.huang.service.TagService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryAll(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);
        return R.ok().put("data", page);
    }


    @GetMapping("/queryByIds")
    public R queryByIds(@RequestParam Map<String, Object> params){
        return tagService.queryByIds(params);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		TagEntity tag = tagService.getById(id);
        return R.ok().put("tag", tag);
    }

    @PostMapping("/save")
    public R save(@RequestBody TagEntity tag){
		tagService.saveTag(tag);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody TagEntity tag){
		tagService.updateById(tag);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		tagService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
