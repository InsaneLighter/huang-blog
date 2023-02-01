package com.huang.controller.front;

import com.huang.entity.CommentEntity;
import com.huang.entity.VisitorEntity;
import com.huang.entity.vo.CommentVo;
import com.huang.service.CommentService;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Time 2023-01-30 18:42
 * Created by Huang
 * className: FrontCommentController
 * Description:
 */
@RestController
@RequestMapping("/front/comment")
public class FrontCommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/queryCommentsTree/{postId}")
    public R queryCommentsTree(@PathVariable String postId){
        List<CommentVo> list = commentService.queryTreeByPostId(postId);
        return R.ok().put("data", list);
    }

    @PostMapping("/like")
    public R like(@RequestBody Map<String,Object> params){
        VisitorEntity entity = commentService.like(params);
        return R.ok().put("data", entity);
    }

    @PostMapping
    public R save(@RequestBody CommentEntity entity){
        commentService.save(entity);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody Map<String,Object> params){
        String uid = (String) params.getOrDefault("uid", "");
        if(!StringUtils.hasText(uid)){
            return R.error("删除评论失败!");
        }
        String id = (String) params.getOrDefault("id", "");
        CommentEntity commentEntity = commentService.getById(id);
        String userId = commentEntity.getUid();
        if (userId != null && userId.equals(uid)) {
            commentService.removeById(id);
            return R.ok();
        }
        return R.error("删除评论失败!");
    }
}
