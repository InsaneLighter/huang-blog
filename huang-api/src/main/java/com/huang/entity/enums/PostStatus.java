package com.huang.entity.enums;

import com.huang.enums.ValueEnum;

/**
 * @Time 2022-04-18 16:07
 * Created by Huang
 * className: PostStatus
 * Description:
 */
public enum PostStatus implements ValueEnum<Integer> {

    PUBLISHED(0),

    DRAFT(1),

    RECYCLE(2);

    private final int value;

    PostStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
