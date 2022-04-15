package com.huang.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.utils.PageUtils;
import com.huang.entity.JournalPatchLogEntity;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-15 10:19:09
 */
public interface JournalPatchLogService extends IService<JournalPatchLogEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

