package com.smkj.shiroAndJwt.dao;


import com.smkj.shiroAndJwt.entiry.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public int isExistUser(String email);
    public int insertUser(User user);
    public User findUserByEmail(String email);
    public int activateWithCode(String email);
    public User findUserByCode(String code);
    public int updateCodeByEmail(String email, String code, String time);
}
