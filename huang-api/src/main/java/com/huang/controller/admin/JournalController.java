package com.huang.controller.admin;

import com.huang.entity.JournalEntity;
import com.huang.service.JournalService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
@RequestMapping("/admin/journal")
public class JournalController {
    @Autowired
    private JournalService journalService;

    @PostMapping("/list")
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = journalService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping
    public R save(@RequestBody JournalEntity journal){
		journalService.saveJournal(journal);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody JournalEntity journal){
		journalService.updateJournal(journal);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		journalService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R uploadAttachments(@RequestPart("file") MultipartFile file) {
        String url = journalService.upload(file);
        return R.ok().put("url",url);
    }
}
