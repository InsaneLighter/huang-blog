package com.huang.controller.admin;

import com.huang.entity.CommentEntity;
import com.huang.service.CommentService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Time 2023-01-30 14:06
 * Created by Huang
 * className: CommentController
 * Description:
 */
@RestController
@RequestMapping("/admin/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/list")
    public R queryPage(@RequestBody Map<String,Object> params){
        PageUtils page = commentService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PutMapping
    public R update(@RequestBody CommentEntity commentEntity){
        commentService.updateById(commentEntity);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
        commentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @PutMapping("/pass")
    public R pass(@RequestBody String ...ids){
        List<CommentEntity> commentEntities = new ArrayList<>();
        Arrays.stream(ids).forEach(id -> {
            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setId(id);
            commentEntity.setStatus(1);
            commentEntities.add(commentEntity);
        });
        boolean flag = commentService.updateBatchById(commentEntities);
        if(flag){
            return R.ok();
        }
        return R.error("批量审核失败！");
    }
}
