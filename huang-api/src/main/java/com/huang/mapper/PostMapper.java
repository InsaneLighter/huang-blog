package com.huang.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yulichang.base.MPJBaseMapper;
import com.huang.entity.PostEntity;
import com.huang.entity.vo.FrontPostVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;

/**
 * 
 * 
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
public interface PostMapper extends MPJBaseMapper<PostEntity> {

    IPage<FrontPostVo> queryByCondition(IPage page, @Param("paramMap") MapperMethod.ParamMap paramMap);
}
