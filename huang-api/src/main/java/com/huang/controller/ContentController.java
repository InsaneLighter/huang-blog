package com.huang.controller;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.huang.entity.ContentEntity;
import com.huang.service.ContentService;
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
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = contentService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		ContentEntity content = contentService.getById(id);
        return R.ok().put("content", content);
    }

    @PostMapping("/save")
    public R save(@RequestBody ContentEntity content){
		contentService.save(content);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody ContentEntity content){
		contentService.updateById(content);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		contentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
