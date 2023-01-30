package com.huang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Time 2023-01-30 14:11
 * Created by Huang
 * className: CommentEntity
 * Description:
 */
@Data
@TableName("comment")
public class CommentEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 父类别id
     */
    private String parentId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户ID
     */
    private String uid;
    /**
     * 审核状态 1 通过 0 未通过
     */
    private Integer status;
    /**
     * 地址
     */
    private String address;
    /**
     * 点赞数量
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer likes;
    /**
     * postId
     */
    private String postId;
    /**
     * 创建人
     */
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
    /**
     * TableField(exist = false) 数据库中不存在的字段
     * JsonInclude(JsonInclude.Include.NON_EMPTY) 字段读取为空的时候不显示
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private List<CommentEntity> reply;
}
