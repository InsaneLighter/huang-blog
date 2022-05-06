package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@TableName("sys_statistics")
public class SysStatisticsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 点赞数量
	 */
	private Integer likes;
	/**
	 * 访客数量
	 */
	private Integer visit;
	/**
	 * 访客IP数量
	 */
	private Integer ipVisit;
	/**
	 * 文章数量
	 */
	private Integer postCount;
	/**
	 * 日志数量
	 */
	private Integer journalCount;
	/**
	 * 分类数量
	 */
	private Integer categoryCount;
	/**
	 * 标签数量
	 */
	private Integer tagCount;
	/**
	 * 建站时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date birthday;
	/**
	 * TableField(exist = false) 数据库中不存在的字段
	 * JsonInclude(JsonInclude.Include.NON_EMPTY) 字段读取为空的时候不显示
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private Integer establishDays;
	/**
	 * 创建人
	 */
	private String createBy = "Huang";
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateBy = "Huang";
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
