package com.huang.controller;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.huang.entity.TagEntity;
import com.huang.service.TagService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;

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

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		TagEntity tag = tagService.getById(id);
        return R.ok().put("tag", tag);
    }

    @PostMapping("/save")
    public R save(@RequestBody TagEntity tag){
		tagService.save(tag);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody TagEntity tag){
		tagService.updateById(tag);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		tagService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
