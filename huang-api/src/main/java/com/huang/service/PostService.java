package com.huang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.PostEntity;
import com.huang.entity.param.BatchUpdateStatusParam;
import com.huang.entity.vo.ContentVo;
import com.huang.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
public interface PostService extends IService<PostEntity> {
    PageUtils queryPage(Map<String, Object> params);

    ContentVo getByPostId(String postId);

    void delete(String[] ids);

    void updateStatusInBatch(BatchUpdateStatusParam batchUpdateStatusParam);

    PageUtils queryByCondition(Map<String, Object> params);

    void like(Map<String, Object> params);
}

