package com.huang.entity.param;

import com.huang.entity.enums.PostStatus;
import lombok.Data;

import java.util.List;

/**
 * @Time 2022-04-21 19:41
 * Created by Huang
 * className: PostUpdateStatusParam
 * Description:
 */
@Data
public class BatchUpdateStatusParam {
    private List<String> ids;
    private PostStatus status;
}
