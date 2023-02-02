package com.huang.entity.vo;

import lombok.Data;
import java.util.Date;

/**
 * @Time 2022-11-20 16:52
 * Created by Huang
 * className: FrontPostVo
 * Description:
 */
@Data
public class FrontPostVo {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String summary;
    private String cover;
    private String category;
    private Integer topPriority;
    private Integer likes;
    private Integer visit;
    private Long comments;
    private Date createTime;
}