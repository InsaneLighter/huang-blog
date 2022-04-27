package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

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
@ToString
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 登录账号
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 性别(0-默认未知,1-男,2-女)
	 */
	private Integer gender;
	/**
	 * 头像地址
	 */
	private String avatar;

	/**
	 *  个人描述
	 */
	private String description;
	/**
	 * 邮件
	 */
	private String email;
	/**
	 * 冻结状态(0-正常,1-冻结)
	 */
	private Integer status;
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
	/**
	 * 是否为admin账号(0-非admin,1-admin)
	 */
	private Integer isAdmin;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private String ip;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private String browser;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private String address;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private String token;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private Date loginTime;

}
