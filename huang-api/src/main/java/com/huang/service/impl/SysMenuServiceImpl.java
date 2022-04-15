package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.SysMenuMapper;
import com.huang.entity.SysMenuEntity;
import com.huang.service.SysMenuService;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuEntity> implements SysMenuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysMenuEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<SysMenuEntity>()
        );
        return new PageUtils(page);
    }

}