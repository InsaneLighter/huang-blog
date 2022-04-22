package com.huang.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.huang.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Time 2022-04-22 12:13
 * Created by Huang
 * className: OssUtil
 * Description:
 */
@Slf4j
@Component
public class OssUtil {
    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private OSSClient client;

    /**
     * 上传文件至阿里云 OSS
     * 文件上传成功,返回文件完整访问路径
     * 文件上传失败,返回 null
     *
     * @param file    待上传文件
     * @return oss 中的相对文件路径
     */
    public String upload(MultipartFile file, String customBucket) {
        try {
            //判断桶是否存在,不存在则创建桶
            if(!client.doesBucketExist(customBucket)){
                client.createBucket(customBucket);
            }
            // 设置权限(公开读)
            client.setBucketAcl(customBucket, CannedAccessControlList.PublicRead);
            // 获取文件名
            //文件名
            String originalFilename = file.getOriginalFilename();
            //新的文件名 = 存储桶文件名_时间戳.后缀名
            assert originalFilename != null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = customBucket + "_" +
                    System.currentTimeMillis() + "_" + format.format(new Date()) + "_" + new Random().nextInt(1000) +
                    originalFilename.substring(originalFilename.lastIndexOf("."));
            PutObjectResult result = client.putObject(customBucket, fileName, file.getInputStream());

            if (result != null) {
                log.info("------OSS文件上传成功------" + fileName);
            }
            return customBucket + "." + ossProperties.getEndpoint()  + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     * @param url
     */
    public void deleteUrl(String url) {
        deleteUrl(url,null);
    }

    /**
     * 删除文件
     * @param url
     */
    public void deleteUrl(String url,String bucket) {
        client.deleteObject(bucket, url);
    }

    /**
     * 删除文件
     * @param fileName
     */
    public void delete(String bucketName,String fileName) {
        client.deleteObject(bucketName, fileName);
    }

    /**
     * 获取文件流
     * @param objectName
     * @param bucket
     * @return
     */
    public InputStream getOssFile(String objectName, String bucket){
        InputStream inputStream = null;
        try{
            OSSObject ossObject = client.getObject(bucket,objectName);
            inputStream = new BufferedInputStream(ossObject.getObjectContent());
        }catch (Exception e){
            log.info("文件获取失败" + e.getMessage());
        }
        return inputStream;
    }

    /**
     * 获取文件外链
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     */
    public String getObjectURL(String bucketName, String objectName, Date expires) {
        try{
            if(client.doesObjectExist(bucketName,objectName)){
                URL url = client.generatePresignedUrl(bucketName,objectName,expires);
                return URLDecoder.decode(url.toString(),"UTF-8");
            }
        }catch (Exception e){
            log.info("文件路径获取失败" + e.getMessage());
        }
        return null;
    }

}
