package com.smkj.shiroAndJwt.service;


import com.smkj.shiroAndJwt.entiry.User;

public interface UserService {
    public int insertUser(User user);
    public User findUserByEmail(String email);

}
