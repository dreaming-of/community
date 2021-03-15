package com.chl.community.service;

import com.chl.community.mapper.DiscussPostMapper;
import com.chl.community.entity.DiscussPost;
import com.chl.community.utils.SensetiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private SensetiveFilter sensetiveFilter;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPost(int user_id, int offset, int limit){
        return discussPostMapper.selectDiscussPost(user_id, offset, limit);
    }

    public int findDiscussPostRows(int user_id){
        return discussPostMapper.selectDiscussPostRows(user_id);
    }

    public int addDiscussPost(DiscussPost discussPost){
        if (discussPost == null)
            throw new IllegalArgumentException("参数不能为空");

        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        discussPost.setTitle(sensetiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensetiveFilter.filter(discussPost.getContent()));

        return discussPostMapper.insertDiscussPost(discussPost);
    }

    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id, int commentCount){
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
