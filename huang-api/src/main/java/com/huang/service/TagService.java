package com.huang.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.utils.PageUtils;
import com.huang.entity.TagEntity;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-13 22:04:28
 */
public interface TagService extends IService<TagEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

