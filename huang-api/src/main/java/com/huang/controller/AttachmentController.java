package com.huang.controller;

import com.huang.entity.AttachmentEntity;
import com.huang.entity.enums.AttachmentType;
import com.huang.service.AttachmentService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-25 16:15:08
 */
@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attachmentService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		AttachmentEntity attachment = attachmentService.getById(id);
        return R.ok().put("attachment", attachment);
    }

    @PostMapping("/save")
    public R save(@RequestBody AttachmentEntity attachment){
		attachmentService.save(attachment);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody AttachmentEntity attachment){
		attachmentService.updateById(attachment);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		attachmentService.delete(Arrays.asList(ids));
        return R.ok();
    }

    @PostMapping("/upload")
    public R uploadAttachment(@RequestPart("file") MultipartFile file) {
        AttachmentEntity entity = attachmentService.upload(file);
        return R.ok().put("data",entity);
    }

    @PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R uploadAttachments(@RequestPart("files") MultipartFile[] files) {
        List<AttachmentEntity> result = new LinkedList<>();
        for (MultipartFile file : files) {
            // Upload single file
            AttachmentEntity attachment = attachmentService.upload(file);
            // Convert and add
            result.add(attachment);
        }

        return R.ok().put("data",result);
    }

    @GetMapping("/listMediaTypes")
    public R listMediaTypes() {
        List<String> list = attachmentService.listAllMediaType();
        return R.ok().put("data",list);
    }

    @GetMapping("/listTypes")
    public R listTypes() {
        List<AttachmentType> attachmentTypes = attachmentService.listAllType();
        return R.ok().put("data",attachmentTypes);
    }
}
