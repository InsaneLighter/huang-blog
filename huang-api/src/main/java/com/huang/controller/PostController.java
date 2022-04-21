package com.huang.controller;

import com.huang.entity.PostEntity;
import com.huang.entity.param.BatchUpdateStatusParam;
import com.huang.entity.vo.ContentVo;
import com.huang.service.PostService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        return R.ok().put("data", page);
    }

    @GetMapping("/get/{postId}")
    public R getByPostId(@PathVariable String postId){
        ContentVo contentVo = postService.getByPostId(postId);
        return R.ok().put("data", contentVo);
    }

    @PutMapping("/updateStatusInBatch")
    public R updateStatusInBatch(@RequestBody BatchUpdateStatusParam batchUpdateStatusParam){
        postService.updateStatusInBatch(batchUpdateStatusParam);
        return R.ok();
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
    public R delete(@RequestBody String ...ids){
        postService.delete(ids);
        return R.ok();
    }

}
