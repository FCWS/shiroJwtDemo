package com.smkj.shiroAndJwt.service;

import com.smkj.shiroAndJwt.entiry.Role;

import java.util.List;

public interface RoleService {

    /**
     * 通过用户id查询用户角色
     * @param id
     * @return
     */
    public List<Role> findRolesByUserId(Integer id);


    /**
     * 注册成功后 初始用户角色为normal
     * @param userId
     * @return
     */
    public int insertDefaultRole(Integer userId);

}
