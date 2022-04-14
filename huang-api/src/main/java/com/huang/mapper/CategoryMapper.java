package com.huang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huang.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 
 * 
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-13 21:46:05
 */
@Mapper
@Repository
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
	
}
