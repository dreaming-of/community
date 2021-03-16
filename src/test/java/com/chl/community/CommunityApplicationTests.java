package com.chl.community;

import com.chl.community.controller.DiscussPostController;
import com.chl.community.entity.DiscussPost;
import com.chl.community.service.DiscussPostService;
import com.chl.community.utils.MailClient;
import com.chl.community.utils.SensetiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {

    }
}
