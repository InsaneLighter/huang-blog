package com.huang.controller.front;

import com.huang.entity.VisitorEntity;
import com.huang.service.VisitorService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 *
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2023-01-30 17:31:13
 */
@RestController
@RequestMapping("/front/visitor")
public class FrontVisitorController {
    @Autowired
    private VisitorService visitorService;

    @GetMapping("/list")
    public R queryPage(@RequestParam Map<String, Object> params){
        PageUtils page = visitorService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        VisitorEntity visitor = visitorService.getById(id);
        return R.ok().put("visitor", visitor);
    }

    @PostMapping
    public R save(@RequestBody VisitorEntity visitor){
        visitorService.save(visitor);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody VisitorEntity visitor){
        visitorService.updateById(visitor);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String[] ids){
        visitorService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @GetMapping("/c")
    public R createTempVisitor(HttpServletRequest request){
        VisitorEntity entity = null;
        try {
            entity = visitorService.createTempVisitor(request);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(entity == null){
                entity = new VisitorEntity();
                entity.setAddress("未知");
                entity.setUsername("未知用户");
            }
        }
        return R.ok().put("data",entity);
    }
}
