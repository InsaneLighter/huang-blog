package com.huang.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Time 2022-11-20 17:34
 * Created by Huang
 * className: FrontJournalVo
 * Description:
 */
@Data
public class FrontJournalVo {
    private String id;
    private String weather;
    private String mood;
    private String content;
    private Integer likes;
    private Integer visit;
    private Date createTime;
}