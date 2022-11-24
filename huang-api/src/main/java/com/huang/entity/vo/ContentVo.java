package com.huang.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huang.entity.enums.PostStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Time 2022-04-21 15:59
 * Created by Huang
 * className: ContentVo
 * Description:
 */
@Data
public class ContentVo {
    private String id;
    private String title;
    private String summary;
    private String originContent;
    private String content;
    private Integer topPriority;
    private PostStatus status;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private List<String> categoryIds;
    private List<String> tagIds;
}
