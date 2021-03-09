package com.chl.community.service;

import com.chl.community.dao.UserMapper;
import com.chl.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int user_id){
        return userMapper.selectById(user_id);
    }
}
