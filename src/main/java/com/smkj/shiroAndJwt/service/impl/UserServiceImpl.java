package com.smkj.shiroAndJwt.service.impl;

import com.smkj.shiroAndJwt.dao.UserMapper;
import com.smkj.shiroAndJwt.entiry.User;
import com.smkj.shiroAndJwt.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    @Override
    public int activateWithCode(String code) {
        return userMapper.activateWithCode(code);
    }

    @Override
    public User findUserByCode(String code) {
        return userMapper.findUserByCode(code);
    }
}
