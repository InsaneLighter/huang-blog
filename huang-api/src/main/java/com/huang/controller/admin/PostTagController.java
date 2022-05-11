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
@RequestMapping("/admin/postTag")
public class PostTagController {
    @Autowired
    private PostTagService postTagService;

    @GetMapping
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = postTagService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping
    public R save(@RequestBody PostTagEntity postTag){
		postTagService.save(postTag);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody PostTagEntity postTag){
		postTagService.updateById(postTag);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		postTagService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
