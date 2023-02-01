package com.huang.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huang.entity.CategoryEntity;
import com.huang.entity.TagEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Time 2022-04-16 18:35
 * Created by Huang
 * className: ContentVo
 * Description:
 */
@Data
public class PostVo {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String summary;
    private Integer topPriority;
    private Integer likes;
    private Integer visit;
    private Long comments;
    private Integer deleted;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private List<CategoryEntity> categories;
    private List<TagEntity> tags;
}
