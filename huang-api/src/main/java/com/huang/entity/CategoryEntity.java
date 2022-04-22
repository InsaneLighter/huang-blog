package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
@Data
@TableName("category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 父类别id
	 */
	private String parentId;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 分类描述
	 */
	private String description;
	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;


	/**
	 * TableField(exist = false) 数据库中不存在的字段
	 * JsonInclude(JsonInclude.Include.NON_EMPTY) 字段读取为空的时候不显示
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private List<CategoryEntity> children;
}
