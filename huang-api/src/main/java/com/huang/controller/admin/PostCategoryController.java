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
@RequestMapping("/postCategory")
public class PostCategoryController {
    @Autowired
    private PostCategoryService postCategoryService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = postCategoryService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		PostCategoryEntity postCategory = postCategoryService.getById(id);
        return R.ok().put("postCategory", postCategory);
    }

    @PostMapping("/save")
    public R save(@RequestBody PostCategoryEntity postCategory){
		postCategoryService.save(postCategory);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody PostCategoryEntity postCategory){
		postCategoryService.updateById(postCategory);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		postCategoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
