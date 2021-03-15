package com.chl.community;

import com.chl.community.controller.DiscussPostController;
import com.chl.community.entity.DiscussPost;
import com.chl.community.service.DiscussPostService;
import com.chl.community.utils.MailClient;
import com.chl.community.utils.SensetiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    private SensetiveFilter sensetiveFilter;

    @Autowired
    private DiscussPostController discussPostController;

    @Test
    void contextLoads() {
        String text = "赌博嫖娼，吸毒傻逼";
        text = sensetiveFilter.filter(text);
        System.out.println(text);
    }
}
