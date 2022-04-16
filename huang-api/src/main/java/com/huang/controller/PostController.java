package com.huang.controller;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.huang.entity.PostEntity;
import com.huang.service.PostService;
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
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = postService.queryPage(params);
        return R.ok().put("page", page);
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
