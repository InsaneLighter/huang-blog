package com.huang.controller.admin;
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
@RequestMapping("/admin/contentPatchLog")
public class ContentPatchLogController {
    @Autowired
    private ContentPatchLogService contentPatchLogService;

    @PostMapping
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = contentPatchLogService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping
    public R save(@RequestBody ContentPatchLogEntity contentPatchLog){
		contentPatchLogService.save(contentPatchLog);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody ContentPatchLogEntity contentPatchLog){
		contentPatchLogService.updateById(contentPatchLog);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		contentPatchLogService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
