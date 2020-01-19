package com.smkj.shiroAndJwt.service.impl;

import com.smkj.shiroAndJwt.dao.RoleMapper;
import com.smkj.shiroAndJwt.entiry.Role;
import com.smkj.shiroAndJwt.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRolesByUserId(Integer userId) {
        return roleMapper.findRolesByUserId(userId);
    }

    @Override
    public int insertDefaultRole(Integer userId) {
        return roleMapper.insertDefaultRole(userId);
    }

}
