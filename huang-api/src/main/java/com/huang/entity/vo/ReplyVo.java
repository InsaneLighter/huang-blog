package com.huang.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Time 2023-01-30 19:07
 * Created by Huang
 * className: ReplyVo
 * Description:
 */
@Data
public class ReplyVo {
    private List<CommentVo> list;
    private Integer total;
}
