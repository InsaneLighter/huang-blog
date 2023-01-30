package com.huang.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.VisitorEntity;
import com.huang.mapper.VisitorMapper;
import com.huang.service.VisitorService;
import com.huang.utils.CommonUtils;
import com.huang.utils.IPUtils;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Service("visitorService")
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, VisitorEntity> implements VisitorService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VisitorEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<VisitorEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public VisitorEntity createTempVisitor(HttpServletRequest request) {
        String ip = IPUtils.getClientIP(request);
        String address = CommonUtils.getCityInfo(ip);
        VisitorEntity visitorEntity = new VisitorEntity();
        visitorEntity.setAddress(address);
        visitorEntity.setIp(ip);
        visitorEntity.setUsername("用户" + RandomUtil.randomInt(100,100000));
        this.save(visitorEntity);
        return visitorEntity;
    }

}