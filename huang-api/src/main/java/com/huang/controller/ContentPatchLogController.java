package com.huang.controller;
import com.huang.entity.ContentPatchLogEntity;
import com.huang.service.ContentPatchLogService;
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
@RequestMapping("/contentPatchLog")
public class ContentPatchLogController {
    @Autowired
    private ContentPatchLogService contentPatchLogService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = contentPatchLogService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		ContentPatchLogEntity contentPatchLog = contentPatchLogService.getById(id);
        return R.ok().put("contentPatchLog", contentPatchLog);
    }

    @PostMapping("/save")
    public R save(@RequestBody ContentPatchLogEntity contentPatchLog){
		contentPatchLogService.save(contentPatchLog);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody ContentPatchLogEntity contentPatchLog){
		contentPatchLogService.updateById(contentPatchLog);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String[] ids){
		contentPatchLogService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
