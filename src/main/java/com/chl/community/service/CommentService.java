package com.chl.community.service;

import com.chl.community.entity.Comment;
import com.chl.community.mapper.CommentMapper;
import com.chl.community.mapper.DiscussPostMapper;
import com.chl.community.utils.CommunityConstant;
import com.chl.community.utils.SensetiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensetiveFilter sensetiveFilter;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<Comment> findCommmentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId){
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        if (comment == null)
            throw new IllegalArgumentException("参数不能为空");

        comment.setContent(sensetiveFilter.filter(HtmlUtils.htmlEscape(comment.getContent())));
        int row = commentMapper.insertComment(comment);

        if (comment.getEntityType() == ENTITY_TYPE_POST){
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);
        }
        return row;
    }
}
