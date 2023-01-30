package com.huang.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.utils.PageUtils;
import com.huang.entity.VisitorEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2023-01-30 17:31:13
 */
public interface VisitorService extends IService<VisitorEntity> {
    PageUtils queryPage(Map<String, Object> params);

    VisitorEntity createTempVisitor(HttpServletRequest request);
}

