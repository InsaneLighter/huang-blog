package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.SysUserMapper;
import com.huang.entity.SysUserEntity;
import com.huang.service.SysUserService;


@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysUserEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<SysUserEntity>()
        );
        return new PageUtils(page);
    }

}