package com.huang.entity.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huang.entity.enums.PostStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

/**
 * @Time 2022-04-21 9:14
 * Created by Huang
 * className: ContentParam
 * Description:
 */
@Data
public class ContentParam {
    /**
     *  postId
     */
    private String id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * md文章内容
     */
    private String originContent;
    /**
     * html文章内容
     */
    private String content;
    /**
     * 文章状态
     */
    private PostStatus status;
    /**
     * 文章概要
     */
    private String summary;
    /**
     * 文章封面
     */
    private String cover;
    /**
     * 优先展示
     */
    private Integer topPriority;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 标签ids
     */
    private Set<String> tagIds;
    /**
     * 分类ids
     */
    private Set<String> categoryIds;

}
