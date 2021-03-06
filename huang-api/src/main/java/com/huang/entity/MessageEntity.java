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
 * @date 2022-04-14 18:25:41
 */
@Data
@TableName("message")
public class MessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 日志内容
	 */
	private String content;
	/**
	 * 点赞数量
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer likes;
	/**
	 * 删除状态(0-正常,1-已删除)
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private Integer deleted;
	/**
	 * 请求IP
	 */
	private String requestIp;
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
