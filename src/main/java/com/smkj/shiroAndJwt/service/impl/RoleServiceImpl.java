package com.smkj.shiroAndJwt.service.impl;

import com.smkj.shiroAndJwt.entiry.Role;
import com.smkj.shiroAndJwt.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public List<Role> findRolesByUserId(Integer id) {
        return null;
    }
}
