package com.huang.controller.front;

import com.huang.entity.PostEntity;
import com.huang.entity.vo.ContentVo;
import com.huang.service.CategoryService;
import com.huang.service.ContentService;
import com.huang.service.PostService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class FrontPostController {
    @Autowired
    private PostService postService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public R category() {
        return R.ok().put("data", categoryService.getCategories());
    }

    @PostMapping("/list")
    public R postList(@RequestBody Map<String, Object> params) {
        PageUtils data = postService.queryByCondition(params);
        return R.ok().put("data", data);
    }

    @GetMapping("/{postId}")
    public R getByPostId(@PathVariable String postId) {
        ContentVo contentVo = postService.getByPostId(postId);
        return R.ok().put("data", contentVo);
    }

    @GetMapping("/visit/{postId}")
    public R visitPost(@PathVariable String postId) {
        PostEntity postEntity = postService.getById(postId);
        Integer visit = postEntity.getVisit();
        postEntity.setVisit(visit == null ? 1 : (visit + 1));
        postService.updateById(postEntity);
        return R.ok();
    }
}