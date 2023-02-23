package com.huang.event;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huang.entity.*;
import com.huang.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Time 2022-05-06 16:52
 * Created by Huang
 * className: BlogEventListener
 * Description:
 */
@Slf4j
@Component
public class BlogEventListener {
    @Autowired
    private SysStatisticsMapper sysStatisticsMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private JournalMapper journalMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    @EventListener
    public void incrSubjectCount(BlogEvent event) {
        QueryWrapper<SysStatisticsEntity> statisticsWrapper = new QueryWrapper<>();
        statisticsWrapper.orderByDesc("create_time");
        SysStatisticsEntity sysStatisticsEntity = sysStatisticsMapper.selectList(statisticsWrapper).stream().findFirst().orElse(null);
        Long postCount = postMapper.selectCount(new QueryWrapper<>());
        Long journalCount = journalMapper.selectCount(new QueryWrapper<>());
        QueryWrapper<CategoryEntity> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.ne("id", 0);
        Long categoryCount = categoryMapper.selectCount(categoryWrapper);
        Long tagCount = tagMapper.selectCount(new QueryWrapper<>());
        if(sysStatisticsEntity == null){
            sysStatisticsEntity = new SysStatisticsEntity();
        }
        Object type = event.getObject();
        if(type instanceof PostEntity){
            sysStatisticsEntity.setPostCount(Math.toIntExact(postCount));
            sysStatisticsMapper.updateById(sysStatisticsEntity);
        }else if(type instanceof JournalEntity){
            sysStatisticsEntity.setJournalCount(Math.toIntExact(journalCount));
            sysStatisticsMapper.updateById(sysStatisticsEntity);
        }else if(type instanceof CategoryEntity){
            sysStatisticsEntity.setCategoryCount(Math.toIntExact(categoryCount));
            sysStatisticsMapper.updateById(sysStatisticsEntity);
        }else if(type instanceof TagEntity){
            sysStatisticsEntity.setTagCount(Math.toIntExact(tagCount));
            sysStatisticsMapper.updateById(sysStatisticsEntity);
        }
    }
}
