package com.huang.controller.front;

import com.huang.entity.AttachmentEntity;
import com.huang.service.AttachmentService;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Time 2023-02-02 17:20
 * Created by Huang
 * className: FrontMeController
 * Description:
 */
@RestController
@RequestMapping("/front/me")
public class FrontMeController {
    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/dayInfo")
    public R dayInfo(){
        return R.ok();
    }

    @GetMapping("/images")
    public R images(){
        List<AttachmentEntity> list = attachmentService.listPicWall();
        return R.ok().put("data",list);
    }

    @GetMapping("/dailyArticle")
    public R dailyArticle(){
        return R.ok();
    }
}
