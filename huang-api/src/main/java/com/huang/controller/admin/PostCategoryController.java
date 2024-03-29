package com.huang.controller.admin;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.huang.entity.PostCategoryEntity;
import com.huang.service.PostCategoryService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-15 10:19:09
 */
@RestController
@RequestMapping("/admin/postCategory")
public class PostCategoryController {
    @Autowired
    private PostCategoryService postCategoryService;

    @PostMapping("/list")
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = postCategoryService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping
    public R save(@RequestBody PostCategoryEntity postCategory){
		postCategoryService.save(postCategory);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody PostCategoryEntity postCategory){
		postCategoryService.updateById(postCategory);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		postCategoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
