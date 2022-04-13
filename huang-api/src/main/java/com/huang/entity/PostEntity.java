package com.huang.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-12 18:28:21
 */
@Data
@TableName("post")
public class PostEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Integer id;
	/**
	 * 文章标题
	 */
	private String title;
	/**
	 * 文章概要
	 */
	private String summary;
	/**
	 * 优先展示
	 */
	private Integer topPriority;
	/**
	 * 文章状态(0-待审核,1-审核通过)
	 */
	private Integer status;
	/**
	 * 点赞数量
	 */
	private Integer likes;
	/**
	 * 访客数量
	 */
	private Integer visit;
	/**
	 * 删除状态(0-正常,1-已删除)
	 */
	private Integer deleted;
	/**
	 * 编辑人
	 */
	private String editBy;
	/**
	 * 编辑时间
	 */
	private Date editTime;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 修改时间
	 */
	private Date updateTime;

}
