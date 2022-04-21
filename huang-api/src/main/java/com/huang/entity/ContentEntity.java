package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.huang.entity.enums.PostStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-14 18:25:41
 */
@Data
@TableName("content")
public class ContentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 文章标题
	 */
	private String title;
	/**
	 * md文章内容
	 */
	private String originContent;
	/**
	 * html文章内容
	 */
	private String content;
	/**
	 * 文字数量
	 */
	private Integer wordCount;
	/**
	 * 当前版本
	 */
	@Version
	@TableField(fill = FieldFill.INSERT)
	private Integer version;
	/**
	 * 文章状态
	 */
	private PostStatus status;
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
