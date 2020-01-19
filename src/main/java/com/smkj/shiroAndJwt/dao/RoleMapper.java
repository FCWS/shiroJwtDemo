package com.smkj.shiroAndJwt.dao;

import com.smkj.shiroAndJwt.entiry.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    public List<Role> findRolesByUserId(Integer userId);
    public int insertDefaultRole(Integer userId);
}
