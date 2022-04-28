package com.huang.entity.param;

import lombok.Data;

/**
 * @Time 2022-04-27 20:38
 * Created by Huang
 * className: PwdParam
 * Description:
 */
@Data
public class PwdParam {
    private String id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
