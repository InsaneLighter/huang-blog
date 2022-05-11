package com.huang.controller.admin;

import com.huang.entity.MailEntity;
import com.huang.utils.MailUtil;
import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Time 2022-05-02 11:17
 * Created by Huang
 * className: MailController
 * Description:
 */
@RestController
@RequestMapping("/admin/mail")
@Slf4j
public class MailController {
    @Autowired
    private MailUtil mailUtil;

    @PostMapping("/send")
    public R sendMail(@RequestBody MailEntity mailEntity){
        String result = mailUtil.send(mailEntity);
        return R.ok().put("result", result);
    }

    @PostMapping("/upload/images")
    public R uploadImages(@RequestPart("file") MultipartFile multipartFile){
        //wangeditor 图片上传即使选择多个 则会分多次请求上传
        String url = mailUtil.upload(multipartFile);
        return R.ok().put("url",url);
    }
}
