package com.huang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.JournalEntity;
import com.huang.utils.PageUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
public interface JournalService extends IService<JournalEntity> {
    PageUtils queryPage(Map<String, Object> params);

    void saveJournal(JournalEntity journal);

    void updateJournal(JournalEntity journal);

    String upload(MultipartFile file);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

