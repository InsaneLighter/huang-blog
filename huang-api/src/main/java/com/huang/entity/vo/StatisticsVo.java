package com.huang.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Time 2022-05-05 15:52
 * Created by Huang
 * className: Statistics
 * Description:
 */
@Data
public class StatisticsVo {
    private Integer postCount;
    private Integer categoryCount;
    private Integer tagCount;
    private Integer visitCount;
    private Integer establishDays;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthday;
}
