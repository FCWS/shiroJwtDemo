package com.smkj.shiroAndJwt.service.impl;

import com.smkj.shiroAndJwt.dao.PermissionMapper;
import com.smkj.shiroAndJwt.entiry.Permission;
import com.smkj.shiroAndJwt.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> findPermissionByRoleId(Integer id) {
        return permissionMapper.findPermissionByRoleId(id);
    }
}
