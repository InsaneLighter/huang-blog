package com.huang.utils;

import com.huang.entity.MailEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Time 2022-05-01 20:16
 * Created by Huang
 * className: MailUtils
 * Description:
 */
@Component
@Slf4j
public class MailUtil {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;


    public String send(MailEntity mailEntity) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from,"Huang");
            helper.setTo(mailEntity.getTos());
            //邮件主题
            helper.setSubject(mailEntity.getSubject());
            //邮件内容拼接
            String content = mailEntity.getContent();
            helper.setText(content, true);
            //添加附件
            MultipartFile[] multipartFiles = mailEntity.getMultipartFiles();
            if (multipartFiles != null) {
                Arrays.stream(multipartFiles).forEach(multipartFile -> {
                    if (multipartFile != null) {
                        try {
                            File file = multipartFileToFile(multipartFile);
                            String fileName = file.getName();
                            helper.addAttachment(fileName, file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            //验证邮箱地址
            helper.setValidateAddresses(true);

            String sentDate = mailEntity.getSentDate();
            if (StringUtils.hasText(sentDate)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(sentDate);
                //定时发送
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mailSender.send(message);
                        log.info("mail send end: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    }
                },date);
            } else {
                mailSender.send(message);
            }
            return String.format("邮件发送成功:%s",Arrays.toString(mailEntity.getTos()));
        } catch (Exception e) {
            return String.format("邮件发送失败:%s",e);
        }
    }

    private File multipartFileToFile(MultipartFile multiFile) throws Exception {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        File file = File.createTempFile(fileName, prefix);
        multiFile.transferTo(file);
        return file;
    }
}
