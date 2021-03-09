package com.chl.community.dao;

import com.chl.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPost(int user_id, int offset, int limit);

    int selectDiscussPostRows(@Param("user_id") int user_id);
}
