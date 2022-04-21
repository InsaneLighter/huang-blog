package com.huang.controller;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.huang.entity.SysMenuEntity;
import com.huang.service.SysMenuService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-15 10:19:09
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysMenuService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		SysMenuEntity sysMenu = sysMenuService.getById(id);
        return R.ok().put("sysMenu", sysMenu);
    }

    @PostMapping("/save")
    public R save(@RequestBody SysMenuEntity sysMenu){
		sysMenuService.save(sysMenu);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody SysMenuEntity sysMenu){
		sysMenuService.updateById(sysMenu);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		sysMenuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
