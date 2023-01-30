package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.CommentEntity;
import com.huang.entity.JournalEntity;
import com.huang.mapper.CommentMapper;
import com.huang.service.CommentService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Time 2023-01-30 14:18
 * Created by Huang
 * className: CommentServiceImpl
 * Description:
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String keyword = (String) params.getOrDefault("keyword", "");
        String startDate = (String) params.getOrDefault("startDate", "");
        String endDate = (String) params.getOrDefault("endDate", "");
        QueryWrapper<CommentEntity> commentWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(startDate)) {
            commentWrapper.ge("create_time", startDate + " 00:00:00");
        }
        if (StringUtils.hasText(endDate)) {
            commentWrapper.le("create_time", endDate + " 23:59:59");
        }
        if (StringUtils.hasText(keyword)) {
            commentWrapper.and(messageEntityQueryWrapper -> {
                messageEntityQueryWrapper.like("content", keyword);
                messageEntityQueryWrapper.like("username", keyword);
                messageEntityQueryWrapper.like("address", keyword);
            });
        }
        String status = (String) params.getOrDefault("status","");
        if(StringUtils.hasText(status)){
            commentWrapper.eq("status", Integer.parseInt(status));
        }
        commentWrapper.orderByDesc("create_time");
        IPage<JournalEntity> page = this.page(new Query().getPage(params), commentWrapper);
        return new PageUtils(page);
    }
}
