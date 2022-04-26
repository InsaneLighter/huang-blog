package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.PostTagEntity;
import com.huang.entity.TagEntity;
import com.huang.exception.BlogException;
import com.huang.mapper.PostTagMapper;
import com.huang.mapper.TagMapper;
import com.huang.service.TagService;
import com.huang.utils.CommonUtils;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, TagEntity> implements TagService {
    @Autowired
    private PostTagMapper postTagMapper;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TagEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<TagEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryAll(Map<String, Object> params) {
        QueryWrapper<TagEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("name");
        List<TagEntity> list = this.list(wrapper);
        Page<TagEntity> page = new Page<>();
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public void saveTag(TagEntity tag) {
        String color = tag.getColor();
        if (!StringUtils.hasText(color) || !color.startsWith("#")) {
            tag.setColor("#efefef");
        }

        String tagName = tag.getName();
        QueryWrapper<TagEntity> tagWrapper = new QueryWrapper<>();
        tagWrapper.eq("name",tagName);
        List<TagEntity> tagEntities = this.list(tagWrapper);
        List<TagEntity> sameEntities = tagEntities.stream().filter(tagEntity -> !tagName.equals(tagEntity.getName())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sameEntities)) {
            this.save(tag);
        }else {
            throw new BlogException("存在重复名称的标签！");
        }
    }

    @Override
    public R queryByIds(Map<String, Object> params) {
        String ids = (String) params.getOrDefault("ids", "");
        Page<PostTagEntity> page = new Page<>();
        boolean compare = false;
        if (StringUtils.hasText(ids)) {
            List<String> tagIds = Arrays.stream(ids.split(",")).collect(Collectors.toList());
            //分类关联文章
            QueryWrapper<PostTagEntity> postTagWrapper = new QueryWrapper<>();
            postTagWrapper.in("tag_id",tagIds);
            List<PostTagEntity> postTagEntities = postTagMapper.selectList(postTagWrapper);
            page.setRecords(postTagEntities);

            QueryWrapper<TagEntity> tagWrapper = new QueryWrapper<>();
            tagWrapper.in("id",tagIds);
            List<TagEntity> tagEntities = this.list(tagWrapper);
            List<String> resultTagIds = tagEntities.stream().map(TagEntity::getId).distinct().collect(Collectors.toList());
            compare = CommonUtils.compare(tagIds, resultTagIds);
        }
        return R.ok().put("data",new PageUtils(page)).put("hasChildren",compare);
    }

}