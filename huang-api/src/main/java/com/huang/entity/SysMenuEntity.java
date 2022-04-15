package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-15 10:19:09
 */
@Data
@TableName("sys_menu")
public class SysMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 上级菜单ID
	 */
	private Integer pid;
	/**
	 * 子菜单数目
	 */
	private Integer subCount;
	/**
	 * 菜单类型
	 */
	private Integer type;
	/**
	 * 菜单标题
	 */
	private String title;
	/**
	 * 组件名称
	 */
	private String name;
	/**
	 * 组件
	 */
	private String component;
	/**
	 * 排序
	 */
	private Integer menuSort;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 链接地址
	 */
	private String path;
	/**
	 * 是否外链(1-是,0-否)
	 */
	private Integer iFrame;
	/**
	 * 缓存(1-是,0-否)
	 */
	private Integer cache;
	/**
	 * 隐藏(1-是,0-否)
	 */
	private Integer hidden;
	/**
	 * 权限
	 */
	private String permission;
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

}
