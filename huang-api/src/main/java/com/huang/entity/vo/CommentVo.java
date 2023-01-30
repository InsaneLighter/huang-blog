package com.huang.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Time 2023-01-30 20:34
 * Created by Huang
 * className: CommentVo
 * Description:
 */
@Data
public class CommentVo {
    private String id;
    private String parentId;
    private String content;
    private String username;
    private String uid;
    private String address;
    private Integer like;
    private String postId;
    private Date createTime;
    private ReplyVo reply;
}
