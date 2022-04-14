package com.huang.controller.admin;
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
 * @date 2022-04-13 22:04:28
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);
        return R.ok().put("page", page);
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
