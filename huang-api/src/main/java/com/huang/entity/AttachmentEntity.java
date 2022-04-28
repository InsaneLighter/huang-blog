package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huang.entity.enums.AttachmentType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-25 16:15:08
 */
@Data
@TableName("attachment")
public class AttachmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;
	/**
	 * 文件key
	 */
	private String fileKey;
	/**
	 * 文件高度
	 */
	private Integer height;
	/**
	 * 文件类型
	 */
	private String mediaType;
	/**
	 * 文件名称
	 */
	private String name;
	/**
	 * 文件路径
	 */
	private String path;
	/**
	 * 文件大小
	 */
	private Long size;
	/**
	 * 文件后缀
	 */
	private String suffix;
	/**
	 * 
	 */
	private String thumbPath;
	/**
	 * 类型
	 */
	private AttachmentType type;
	/**
	 * 宽度
	 */
	private Integer width;
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
