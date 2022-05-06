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
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = contentService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		ContentEntity content = contentService.getById(id);
        return R.ok().put("content", content);
    }

    @PostMapping("/save")
    public R save(@RequestBody ContentParam contentParam){
		contentService.saveArticle(contentParam);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody ContentParam contentParam){
		contentService.updateArticle(contentParam);
        return R.ok();
    }

    @DeleteMapping("/delete")
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
