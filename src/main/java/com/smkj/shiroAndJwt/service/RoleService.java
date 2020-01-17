package com.smkj.shiroAndJwt.service;

import com.smkj.shiroAndJwt.entiry.Role;

import java.util.List;

public interface RoleService {
    public List<Role> findRolesByUserId(Integer id);
}
