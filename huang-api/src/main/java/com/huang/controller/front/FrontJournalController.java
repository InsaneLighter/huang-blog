package com.huang.controller.front;

import com.huang.service.JournalService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Time 2022-11-20 17:32
 * Created by Huang
 * className: FrontJournalController
 * Description:
 */
@Slf4j
@RestController
@RequestMapping("/front/journal")
public class FrontJournalController {
    @Autowired
    private JournalService journalService;

    @PostMapping("/list")
    public R list(@RequestBody Map<String,Object> params){
        PageUtils data = journalService.queryPageByCondition(params);
        return R.ok().put("data",data);
    }

    @PostMapping("/like")
    public R like(@RequestBody Map<String,Object> params){
        journalService.like(params);
        return R.ok();
    }
}