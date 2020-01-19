package com.smkj.shiroAndJwt.dao;

import com.smkj.shiroAndJwt.entiry.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    public List<Permission> findPermissionByRoleId(Integer id);
}
