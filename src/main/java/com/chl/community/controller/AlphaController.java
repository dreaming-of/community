package com.chl.community.controller;

import com.chl.community.entity.JsonResult;
import com.chl.community.entity.User;
import com.chl.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@RestController
public class AlphaController {

    @GetMapping("test")
    public Object test() {
        JsonResult<User> result = new JsonResult<>();
        User u = new User();
        u.setId(1);
        u.setCreateTime(new Date());
        u.setHeaderUrl("www.baidu.com");
        u.setActivationCode(UUID.randomUUID().toString());
        u.setStatus(1);
        u.setType(1);
        result.setData(u);
        result.setCode("200");
        result.setMsg("success");
        return result;
    }

}
