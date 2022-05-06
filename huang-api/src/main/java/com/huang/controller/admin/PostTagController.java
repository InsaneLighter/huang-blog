package com.huang.controller.admin;
import com.huang.entity.PostTagEntity;
import com.huang.service.PostTagService;
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
@RequestMapping("/postTag")
public class PostTagController {
    @Autowired
    private PostTagService postTagService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = postTagService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		PostTagEntity postTag = postTagService.getById(id);
        return R.ok().put("postTag", postTag);
    }

    @PostMapping("/save")
    public R save(@RequestBody PostTagEntity postTag){
		postTagService.save(postTag);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody PostTagEntity postTag){
		postTagService.updateById(postTag);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		postTagService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
