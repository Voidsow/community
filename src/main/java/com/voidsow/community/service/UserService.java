package com.voidsow.community.service;

import com.voidsow.community.entity.User;
import com.voidsow.community.entity.UserExample;
import com.voidsow.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
