package com.smkj.shiroAndJwt.service;


import com.smkj.shiroAndJwt.entiry.Permission;

import java.util.List;

public interface PermissionService {
    public List<Permission> findPermissionByRoleId(Integer id);
}
