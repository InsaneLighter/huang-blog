package com.huang.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.utils.PageUtils;
import com.huang.entity.TagEntity;
import com.huang.utils.R;

import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
public interface TagService extends IService<TagEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryAll();

    void saveTag(TagEntity tag);

    R queryByIds(Map<String, Object> params);

    TagEntity queryByName(String name);
}

