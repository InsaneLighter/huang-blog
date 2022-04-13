package com.huang.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.utils.PageUtils;
import com.huang.entity.CategoryEntity;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-13 21:46:05
 */
public interface CategoryService extends IService<CategoryEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

