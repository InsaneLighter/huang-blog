package com.huang.controller;

import com.huang.entity.MailEntity;
import com.huang.utils.MailUtil;
import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Time 2022-05-02 11:17
 * Created by Huang
 * className: MailController
 * Description:
 */
@RestController
@RequestMapping("/mail")
@Slf4j
public class MailController {
    @Autowired
    private MailUtil mailUtil;

    @PostMapping("/send")
    public R sendMail(MailEntity mailEntity){
        String result = mailUtil.send(mailEntity);
        return R.ok().put("result", result);
    }
}
