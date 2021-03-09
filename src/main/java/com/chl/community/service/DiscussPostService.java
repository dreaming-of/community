package com.chl.community.service;

import com.chl.community.dao.DiscussPostMapper;
import com.chl.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPost(int user_id, int offset, int limit){
        return discussPostMapper.selectDiscussPost(user_id, offset, limit);
    }

    public int findDiscussPostRows(int user_id){
        return discussPostMapper.selectDiscussPostRows(user_id);
    }
}
