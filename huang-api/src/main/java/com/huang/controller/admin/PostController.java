package com.huang.controller.admin;

import com.huang.utils.R;
import com.huang.entity.PostEntity;
import com.huang.service.PostService;
import com.huang.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-12 18:28:21
 */
@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = postService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		PostEntity post = postService.getById(id);
        return R.ok().put("post", post);
    }

    @PostMapping("/save")
    public R save(@RequestBody PostEntity post){
		postService.save(post);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody PostEntity post){
		postService.updateById(post);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		postService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
