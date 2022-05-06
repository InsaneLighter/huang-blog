package com.huang.controller.admin;
import com.huang.entity.PostContentEntity;
import com.huang.service.PostContentService;
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
 * @date 2022-04-15 10:19:09
 */
@RestController
@RequestMapping("/postContent")
public class PostContentController {
    @Autowired
    private PostContentService postContentService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = postContentService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		PostContentEntity postContent = postContentService.getById(id);
        return R.ok().put("postContent", postContent);
    }

    @PostMapping("/save")
    public R save(@RequestBody PostContentEntity postContent){
		postContentService.save(postContent);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody PostContentEntity postContent){
		postContentService.updateById(postContent);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		postContentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
