package com.chl.community;

import com.chl.community.dao.DiscussPostMapper;
import com.chl.community.entity.DiscussPost;
import com.chl.community.entity.User;
import com.chl.community.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        User user = userService.findUserById(11);
        System.out.println(user);
    }

}
