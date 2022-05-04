package com.huang.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MailEntity {
    @NotBlank(message = "邮件收件人不能为空")
    private String tos;
    @NotBlank(message = "邮件主题不能为空")
    private String subject;
    @NotBlank(message = "邮件内容不能为空")
    private String content;
    private String sentDate;
}