package com.chl.community.controller;

import com.chl.community.annotation.LoginRequired;
import com.chl.community.entity.User;
import com.chl.community.service.UserService;
import com.chl.community.utils.CommunityUtil;
import com.chl.community.utils.HostHolder;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String comtext;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage == null){
            model.addAttribute("error", "还没有选择文件");
            return "/site/setting";
        }
        String originalFilename = headerImage.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error", "文件格式不确定");
            return "/site/setting";
        }
        String newFilename = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + newFilename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("上传文件失败： " + e.getMessage());
            throw new RuntimeException("上传文件失败");
        }
        User user = hostHolder.getUser();
        String headerUrl = domain + comtext + "/user/header/" + newFilename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:index";
    }

    @GetMapping("/header/{Filename}")
    public void getHeader(@PathVariable("Filename") String filename, HttpServletResponse response){
        filename = uploadPath + "/" + filename;
        String suffix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try(
                OutputStream out = response.getOutputStream();
                FileInputStream in = new FileInputStream(filename)
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while((b = in.read(buffer)) != -1){
                out.write(buffer,0, b);
            }
        } catch (IOException e) {
            log.error("获取头像失败: " + e.getMessage());
        }
    }
}
