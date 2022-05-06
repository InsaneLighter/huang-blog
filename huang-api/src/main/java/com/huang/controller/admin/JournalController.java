package com.huang.controller.admin;
import com.huang.entity.JournalEntity;
import com.huang.service.JournalService;
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
 * @date 2022-04-14 18:25:41
 */
@RestController
@RequestMapping("/journal")
public class JournalController {
    @Autowired
    private JournalService journalService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = journalService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		JournalEntity journal = journalService.getById(id);
        return R.ok().put("journal", journal);
    }

    @PostMapping("/save")
    public R save(@RequestBody JournalEntity journal){
		journalService.save(journal);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody JournalEntity journal){
		journalService.updateById(journal);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		journalService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
