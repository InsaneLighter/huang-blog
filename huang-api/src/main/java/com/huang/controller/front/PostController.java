package com.huang.controller.front;

import com.huang.entity.ContentEntity;
import com.huang.entity.PostEntity;
import com.huang.entity.vo.FrontPostVo;
import com.huang.service.CategoryService;
import com.huang.service.ContentService;
import com.huang.service.PostService;
import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Time 2022-11-20 16:40
 * Created by Huang
 * className: PostController
 * Description:
 */
@Slf4j
@RestController
@RequestMapping("/front/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public R category(){
        return R.ok().put("data",categoryService.list());
    }

    @PostMapping("/list")
    public R postList(@RequestParam Map<String, Object> params){
        List<FrontPostVo> frontPostVoList =  postService.queryByCondition(params);
        return R.ok().put("data",frontPostVoList);
    }

    @GetMapping("/{postId}")
    public R detail(@PathVariable String postId){
        return R.ok().put("data",contentService.getById(postId));
    }
}