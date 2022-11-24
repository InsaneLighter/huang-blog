package com.huang.controller.admin;
import com.huang.entity.JournalPatchLogEntity;
import com.huang.service.JournalPatchLogService;
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
@RequestMapping("/admin/journalPatchLog")
public class JournalPatchLogController {
    @Autowired
    private JournalPatchLogService journalPatchLogService;

    @PostMapping("/list")
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = journalPatchLogService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping
    public R save(@RequestBody JournalPatchLogEntity journalPatchLog){
		journalPatchLogService.save(journalPatchLog);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody JournalPatchLogEntity journalPatchLog){
		journalPatchLogService.updateById(journalPatchLog);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		journalPatchLogService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
