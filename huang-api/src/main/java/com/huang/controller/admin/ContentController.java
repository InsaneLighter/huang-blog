package com.huang.controller.admin;

import com.huang.entity.ContentEntity;
import com.huang.entity.param.ContentParam;
import com.huang.service.ContentService;
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
@RequestMapping("/admin/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = contentService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping
    public R save(@RequestBody ContentParam contentParam){
		contentService.saveArticle(contentParam);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody ContentParam contentParam){
		contentService.updateArticle(contentParam);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		contentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R uploadAttachments(@RequestPart("file") MultipartFile file) {
        String url = contentService.upload(file);
        return R.ok().put("url",url);
    }
}
