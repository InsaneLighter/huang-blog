package com.huang.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.enums.AttachmentType;
import com.huang.utils.PageUtils;
import com.huang.entity.AttachmentEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-25 16:15:08
 */
public interface AttachmentService extends IService<AttachmentEntity> {
    PageUtils queryPage(Map<String, Object> params);

    AttachmentEntity upload(MultipartFile file);

    List<String> listAllMediaType();

    List<AttachmentType> listAllType();

    void delete(List<String> list);

    AttachmentEntity uploadPicWall(MultipartFile file);

    List<AttachmentEntity> listPicWall();
}

