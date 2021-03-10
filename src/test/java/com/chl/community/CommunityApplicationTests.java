package com.chl.community;

import com.chl.community.entity.DiscussPost;
import com.chl.community.service.DiscussPostService;
import com.chl.community.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    private DiscussPostService discussPostService;

    @Test
    void contextLoads() {
        List<DiscussPost> list = discussPostService.findDiscussPost(0, 0, 10);
        for (DiscussPost d : list)
            System.out.println(d);
    }

}
