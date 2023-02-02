package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.huang.entity.enums.PostStatus;
import lombok.Data;
import lombok.ToString;

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
@ToString
@TableName("post")
public class PostEntity implements Serializable {
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
	 * 文章概要
	 */
	private String summary;
	/**
	 * 文章封面
	 */
	private String cover;
	/**
	 * 优先展示
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer topPriority;
	/**
	 * 文章状态
	 */
	private PostStatus status;
	/**
	 * 点赞数量
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer likes;
	/**
	 * 访客数量
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer visit;
	/**
	 * 删除状态(0-正常,1-已删除)
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;
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
