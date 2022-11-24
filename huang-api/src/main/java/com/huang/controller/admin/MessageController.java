package com.huang.controller.admin;
import com.huang.entity.MessageEntity;
import com.huang.service.MessageService;
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
@RequestMapping("/admin/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/list")
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = messageService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping
    public R save(@RequestBody MessageEntity message){
		messageService.save(message);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody MessageEntity message){
		messageService.updateById(message);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		messageService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
