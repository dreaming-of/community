package com.chl.community.controller;

import com.chl.community.entity.DiscussPost;
import com.chl.community.entity.Page;
import com.chl.community.entity.User;
import com.chl.community.service.DiscussPostService;
import com.chl.community.service.LikeService;
import com.chl.community.service.UserService;
import com.chl.community.utils.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/index")
    public String index(Model model, Page page){
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPost(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussposts = new ArrayList<>();
        if(list != null){
            for (DiscussPost d : list){
                Map<String, Object> map = new HashMap<>();
                map.put("post", d);
                User u = userService.findUserById(d.getUserId());
                map.put("user", u);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, d.getId());
                map.put("likeCount", likeCount);
                discussposts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussposts);
        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "/error/500";
    }
}
