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
@TableName("sys_log")
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 日志描述
	 */
	private String description;
	/**
	 * 日志类型
	 */
	private String logType;
	/**
	 * 方法名称
	 */
	private String method;
	/**
	 * 请求参数
	 */
	private String params;
	/**
	 * 请求IP
	 */
	private String requestIp;
	/**
	 * 请求时长
	 */
	private Integer time;
	/**
	 * 操作用户
	 */
	private String username;
	/**
	 * IP来源
	 */
	private String address;
	/**
	 * 浏览器
	 */
	private String browser;
	/**
	 * 异常详情
	 */
	private String exceptionDetail;
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
