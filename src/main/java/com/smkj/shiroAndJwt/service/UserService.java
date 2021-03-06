package com.smkj.shiroAndJwt.service;


import com.smkj.shiroAndJwt.entiry.User;

public interface UserService {
    public int insertUser(User user);
    public User findUserByEmail(String email);
    public int activateWithCode(String code);
    public User findUserByCode(String code);
    public int updateCodeByEmail(String email, String code, String time);

}
