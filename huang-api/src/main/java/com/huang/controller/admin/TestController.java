package com.huang.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Time 2022-04-12 14:28
 * Created by Huang
 * className: TestController
 * Description:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(value = {"","index"})
    public String test(){
        return "ok";
    }
}
