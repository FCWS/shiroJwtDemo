package com.smkj.shiroAndJwt.service;


import com.smkj.shiroAndJwt.entiry.Permission;

public interface PermissionService {
    public Permission findPermissionByRoleId(Integer id);
}
