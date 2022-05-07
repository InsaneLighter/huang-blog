package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.JournalEntity;
import com.huang.entity.JournalPatchLogEntity;
import com.huang.event.BlogEvent;
import com.huang.mapper.JournalMapper;
import com.huang.mapper.JournalPatchLogMapper;
import com.huang.service.JournalService;
import com.huang.utils.MinioUtil;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Service("journalService")
public class JournalServiceImpl extends ServiceImpl<JournalMapper, JournalEntity> implements JournalService {
    @Autowired
    private JournalMapper journalMapper;
    @Autowired
    private JournalPatchLogMapper journalPatchLogMapper;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MinioUtil minioUtil;
    @Value("${minio.journal.bucketName}")
    private String bucketName;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String keyword = (String) params.getOrDefault("keyword", "");
        String startDate = (String) params.getOrDefault("startDate", "");
        String endDate = (String) params.getOrDefault("endDate", "");
        QueryWrapper<JournalEntity> journalWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            journalWrapper.like("weather", keyword)
                    .or().like("mood", keyword)
                    .or().like("content", keyword);
        }
        if(StringUtils.hasText(startDate)){
            journalWrapper.ge("create_time",startDate);
        }
        if(StringUtils.hasText(endDate)){
            journalWrapper.le("create_time",endDate);
        }
        IPage<JournalEntity> page = this.page(new Query().getPage(params), journalWrapper);
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJournal(JournalEntity journal) {
        //journal
        journalMapper.insert(journal);
        applicationContext.publishEvent(new BlogEvent(this,journal));
        //journalPatch
        JournalPatchLogEntity journalPatchLogEntity = new JournalPatchLogEntity();
        journalPatchLogEntity.setSourceId(journal.getId());
        journalPatchLogEntity.setOriginalContent(journal.getContent());
        journalPatchLogMapper.insert(journalPatchLogEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJournal(JournalEntity journal) {
        journalMapper.updateById(journal);
        //journalPatch
        JournalPatchLogEntity journalPatchLogEntity = new JournalPatchLogEntity();
        journalPatchLogEntity.setSourceId(journal.getId());
        journalPatchLogEntity.setOriginalContent(journal.getContent());
        journalPatchLogMapper.insert(journalPatchLogEntity);
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            return minioUtil.uploadFile(multipartFile, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}