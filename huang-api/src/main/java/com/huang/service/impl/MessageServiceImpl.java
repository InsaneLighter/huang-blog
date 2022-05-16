package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.JournalEntity;
import com.huang.entity.MessageEntity;
import com.huang.mapper.MessageMapper;
import com.huang.service.MessageService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String keyword = (String) params.getOrDefault("keyword", "");
        String startDate = (String) params.getOrDefault("startDate", "");
        String endDate = (String) params.getOrDefault("endDate", "");
        QueryWrapper<MessageEntity> messageWrapper = new QueryWrapper<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.hasText(startDate)) {
            Date date = new Date(Long.parseLong(startDate));
            messageWrapper.ge("create_time", sdf.format(date));
        }
        if (StringUtils.hasText(endDate)) {
            Date date = new Date(Long.parseLong(endDate));
            messageWrapper.le("create_time", sdf.format(date) + " 23:59:59");
        }
        if (StringUtils.hasText(keyword)) {
            messageWrapper.and(messageEntityQueryWrapper -> {
                messageEntityQueryWrapper.like("weather", keyword);
                messageEntityQueryWrapper.like("mood", keyword);
                messageEntityQueryWrapper.like("content", keyword);
            });
        }
        messageWrapper.orderByDesc("create_time");
        IPage<JournalEntity> page = this.page(new Query().getPage(params), messageWrapper);
        return new PageUtils(page);
    }

}