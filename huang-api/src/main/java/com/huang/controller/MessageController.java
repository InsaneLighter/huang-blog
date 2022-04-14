package com.huang.controller;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.huang.entity.MessageEntity;
import com.huang.service.MessageService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = messageService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		MessageEntity message = messageService.getById(id);
        return R.ok().put("message", message);
    }

    @PostMapping("/save")
    public R save(@RequestBody MessageEntity message){
		messageService.save(message);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody MessageEntity message){
		messageService.updateById(message);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		messageService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
