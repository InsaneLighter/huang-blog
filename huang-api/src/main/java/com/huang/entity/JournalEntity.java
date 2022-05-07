package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("journal")
public class JournalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 天气
	 */
	private String weather;
	/**
	 * 心情
	 */
	private String mood;
	/**
	 * 日志内容
	 */
	private String content;
	/**
	 * avatar
	 */
	private String avatar;
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
	 * 当前版本
	 */
	@Version
	@TableField(fill = FieldFill.INSERT)
	private Integer version;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
