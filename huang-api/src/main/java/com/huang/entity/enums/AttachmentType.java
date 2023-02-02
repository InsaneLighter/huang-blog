package com.huang.entity.enums;

import com.huang.enums.ValueEnum;

/**
 * @Time 2022-04-25 16:55
 * Created by Huang
 * className: AttachmentType
 * Description:
 */
public enum AttachmentType implements ValueEnum<Integer> {
    /**
     * 服务器
     */
    LOCAL(0),


    /**
     * 阿里云
     */
    ALIOSS(1),

    /**
     * MINIO
     */
    MINIO(2),

    /**
     * PICWALL
     */
    PICWALL(3);

    private final Integer value;

    AttachmentType(Integer value) {
        this.value = value;
    }

    /**
     * Get enum value.
     *
     * @return enum value
     */
    @Override
    public Integer getValue() {
        return value;
    }
}
