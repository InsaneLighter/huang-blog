package com.huang.vo;

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

    private Integer topPriority;

    private String status;

    private Integer likes;

    private Integer visit;

    private Date createTime;

    private List<CategoryEntity> categories;

    private List<TagEntity> tags;


}
