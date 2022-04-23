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
 * @date 2022-04-14 18:25:41
 */
public interface CategoryService extends IService<CategoryEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryAll(Map<String, Object> params);

    PageUtils queryByIds(Map<String, Object> params);
}

