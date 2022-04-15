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
@TableName("journal_patch_log")
public class JournalPatchLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 关联journal主键id
	 */
	private Integer sourceId;
	/**
	 * 日志发布时间
	 */
	private Date publishTime;
	/**
	 * 日志内容
	 */
	private String originalContent;
	/**
	 * 当前版本
	 */
	private String version;
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

}
