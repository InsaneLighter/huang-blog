package com.huang.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.CommentEntity;
import com.huang.entity.JournalEntity;
import com.huang.entity.VisitorEntity;
import com.huang.entity.vo.CommentVo;
import com.huang.entity.vo.ReplyVo;
import com.huang.mapper.CommentMapper;
import com.huang.mapper.VisitorMapper;
import com.huang.service.CommentService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Time 2023-01-30 14:18
 * Created by Huang
 * className: CommentServiceImpl
 * Description:
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {
    @Autowired
    private VisitorMapper visitorMapper;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String keyword = (String) params.getOrDefault("keyword", "");
        String startDate = (String) params.getOrDefault("startDate", "");
        String endDate = (String) params.getOrDefault("endDate", "");
        QueryWrapper<CommentEntity> commentWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(startDate)) {
            commentWrapper.ge("create_time", startDate + " 00:00:00");
        }
        if (StringUtils.hasText(endDate)) {
            commentWrapper.le("create_time", endDate + " 23:59:59");
        }
        if (StringUtils.hasText(keyword)) {
            commentWrapper.and(messageEntityQueryWrapper -> {
                messageEntityQueryWrapper.like("content", keyword);
                messageEntityQueryWrapper.like("username", keyword);
                messageEntityQueryWrapper.like("address", keyword);
            });
        }
        String status = (String) params.getOrDefault("status","");
        if(StringUtils.hasText(status)){
            commentWrapper.eq("status", Integer.parseInt(status));
        }
        commentWrapper.orderByDesc("create_time");
        IPage<JournalEntity> page = this.page(new Query().getPage(params), commentWrapper);
        return new PageUtils(page);
    }

    @Override
    public List<CommentVo> queryTreeByPostId(String postId) {
        QueryWrapper<CommentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        wrapper.eq("post_id", postId);
        List<CommentEntity> list = this.list(wrapper);
        List<CommentVo> commentVos = list.stream().map(item -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(item, commentVo);
            return commentVo;
        }).collect(Collectors.toList());
        //获取父节点
        List<CommentVo> collect = commentVos.stream().filter(t -> !StringUtils.hasText(t.getParentId())).peek(
                m -> {
                    List<CommentVo> children = getChildren(m, commentVos);
                    ReplyVo replyVo = new ReplyVo();
                    replyVo.setTotal(children.size());
                    replyVo.setList(children);
                    m.setReply(replyVo);
                })
                .collect(Collectors.toList());
        System.out.println(JSON.toJSONString(collect));
        return collect;
    }

    @Override
    public void like(Map<String, Object> params) {
        String uid = (String) params.getOrDefault("uid", "");
        String commentId = (String) params.getOrDefault("id", "");
        VisitorEntity visitorEntity = visitorMapper.selectById(uid);
        String likeids = visitorEntity.getLikeids();
        boolean like = true;
        Set<String> likeIds;
        if (StringUtils.hasText(likeids)) {
            likeIds = new HashSet<>(Arrays.asList(likeids.split(",")));
            if(likeids.contains(commentId)){
                like = false;
                likeIds.remove(commentId);
            }else {
                likeIds.add(commentId);
            }
        }else {
            likeIds = new HashSet<>(Collections.singletonList(commentId));
        }
        String likeString = String.join(",", likeIds);
        visitorEntity.setLikeids(likeString);
        visitorMapper.updateById(visitorEntity);

        CommentEntity item = this.getById(commentId);
        item.setLikes(like?item.getLikes()+1:item.getLikes()-1);
        this.updateById(item);
    }

    /**
     * 递归查询子节点
     * @param root  根节点
     * @param all   所有节点
     * @return 根节点信息
     */
    private List<CommentVo> getChildren(CommentVo root, List<CommentVo> all) {
        return all.stream().filter(t -> Objects.equals(t.getParentId(), root.getId())).peek(
                m -> {
                    List<CommentVo> children = getChildren(m, all);
                    ReplyVo replyVo = new ReplyVo();
                    replyVo.setTotal(children.size());
                    replyVo.setList(children);
                    m.setReply(replyVo);
                }
        ).collect(Collectors.toList());
    }
}
