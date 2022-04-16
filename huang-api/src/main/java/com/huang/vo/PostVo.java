package com.huang.vo;

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

    private String status;

    private List<String> categoryName;

    private List<String> tagName;

    private Integer visit;

    private Date createTime;
}
