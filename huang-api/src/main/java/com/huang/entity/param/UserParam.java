package com.huang.entity.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Time 2022-04-26 15:48
 * Created by Huang
 * className: UserParam
 * Description:
 */
@Data
public class UserParam {
    private String username;

    private String password;

    private String code;

    private String uuid = "";
}
