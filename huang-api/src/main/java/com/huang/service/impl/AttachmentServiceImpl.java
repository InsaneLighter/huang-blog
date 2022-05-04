package com.huang.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.AttachmentEntity;
import com.huang.entity.SysUserEntity;
import com.huang.entity.enums.AttachmentType;
import com.huang.exception.BlogException;
import com.huang.mapper.AttachmentMapper;
import com.huang.mapper.SysUserMapper;
import com.huang.service.AttachmentService;
import com.huang.utils.ImageUtils;
import com.huang.utils.MinioUtil;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@Service("attachmentService")
@Slf4j
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, AttachmentEntity> implements AttachmentService {
    private final MediaType IMAGE_TYPE = MediaType.valueOf("image/*");
    @Autowired
    private MinioUtil minioUtil;
    @Value("${minio.attachment.bucketName}")
    private String bucketName;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<AttachmentEntity> attachmentWrapper = new QueryWrapper<>();
        String keyword = (String) params.getOrDefault("keyword", "");
        if(StringUtils.hasText(keyword)){
            attachmentWrapper.like("name",keyword);
        }
        String mediaType = (String) params.getOrDefault("mediaType", "");
        if(StringUtils.hasText(mediaType)){
            attachmentWrapper.eq("media_type",mediaType);
        }
        IPage<AttachmentEntity> page = this.page(
                new Query().getPage(params),
                attachmentWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public AttachmentEntity upload(MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");
        AttachmentType attachmentType = AttachmentType.MINIO;
        AttachmentEntity attachment = uploadByAttachmentType(file, attachmentType);
        this.save(attachment);
        return attachment;
    }

    @Override
    public List<String> listAllMediaType() {
        List<AttachmentEntity> list = this.list();
        return list.stream().map(AttachmentEntity::getMediaType).distinct().collect(Collectors.toList());
    }

    @Override
    public List<AttachmentType> listAllType() {
        List<AttachmentEntity> list = this.list();
        return list.stream().map(AttachmentEntity::getType).distinct().collect(Collectors.toList());
    }

    @Override
    public void delete(List<String> list) {
        List<AttachmentEntity> attachmentEntities = this.listByIds(list);
        List<String> filePaths = new ArrayList<>();
        List<String> fileNames = attachmentEntities.stream().map(attachmentEntity -> {
            String path = attachmentEntity.getPath();
            filePaths.add(path);
            int index = path.lastIndexOf("/");
            return path.substring(index + 1);
        }).collect(Collectors.toList());

        //关联已经使用的图像
        QueryWrapper<SysUserEntity> sysUserWrapper = new QueryWrapper<>();
        sysUserWrapper.in("avatar",filePaths);
        List<SysUserEntity> sysUserEntities = sysUserMapper.selectList(sysUserWrapper);
        if (sysUserEntities.size() > 0) {
            StringBuilder builder = new StringBuilder();
            sysUserEntities.stream().forEach(sysUserEntity -> {
                String avatar = sysUserEntity.getAvatar();
                int i = avatar.lastIndexOf("/");
                builder.append(avatar.substring(i+1)).append(" ");
            });
            throw new BlogException(String.format("选中图像 %s 被使用到！", builder));
        }
        boolean remove = this.removeByIds(list);
        if (remove) {
            fileNames.forEach(fileName -> {
                try {
                    minioUtil.removeObject(bucketName,fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private AttachmentEntity uploadByAttachmentType(MultipartFile file, AttachmentType attachmentType) {
        AttachmentEntity entity = new AttachmentEntity();
        String originalFilename = file.getOriginalFilename();
        entity.setName(originalFilename);
        entity.setSize(file.getSize());
        entity.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())).getType());
        entity.setType(attachmentType);
        entity.setSuffix(FileUtil.getSuffix(originalFilename));
        String filePath = null;
        try {
            Optional<Bucket> bucket = minioUtil.getBucket(bucketName);
            if (!bucket.isPresent()) {
                minioUtil.createBucket(bucketName);
            }
            filePath = minioUtil.uploadFile(file, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        entity.setPath(filePath);
        entity.setFileKey(filePath);
        handleImageMetadata(file, entity);
        return entity;
    }

    void handleImageMetadata(@NonNull MultipartFile file,
                             @NonNull AttachmentEntity attachmentEntity
                             ) {
        if (isImageType(file)) {
            try (InputStream is = file.getInputStream()) {
                String extension = attachmentEntity.getSuffix();
                if (ImageUtils.EXTENSION_ICO.equals(extension)) {
                    BufferedImage icoImage =
                            ImageUtils.getImageFromFile(is, extension);
                    attachmentEntity.setWidth(icoImage.getWidth());
                    attachmentEntity.setHeight(icoImage.getHeight());
                } else {
                    ImageReader image =
                            ImageUtils.getImageReaderFromFile(is, extension);
                    attachmentEntity.setWidth(image.getWidth(0));
                    attachmentEntity.setHeight(image.getHeight(0));
                }
            } catch (IOException | OutOfMemoryError e) {
                log.warn("Failed to fetch image meta data", e);
            }
        }
        if (!StringUtils.hasText(attachmentEntity.getThumbPath())) {
            attachmentEntity.setThumbPath(attachmentEntity.getPath());
        }
    }

    private boolean isImageType(@NonNull MultipartFile file) {
        String mediaType = file.getContentType();
        return mediaType != null && IMAGE_TYPE.includes(MediaType.valueOf(mediaType));
    }
}