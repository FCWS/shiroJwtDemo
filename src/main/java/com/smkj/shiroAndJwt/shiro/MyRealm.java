package com.smkj.shiroAndJwt.shiro;

import com.smkj.shiroAndJwt.entiry.Permission;
import com.smkj.shiroAndJwt.entiry.Role;
import com.smkj.shiroAndJwt.entiry.User;
import com.smkj.shiroAndJwt.service.PermissionService;
import com.smkj.shiroAndJwt.service.RoleService;
import com.smkj.shiroAndJwt.service.UserService;
import com.smkj.shiroAndJwt.util.JWTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LogManager.getLogger(MyRealm.class);

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String email = JWTUtil.getEmail(principals.toString());

        List<String> roleStringList = new ArrayList<>();
        List<String> permissionStringList = new ArrayList<>();

        // 1、通过email拿到用户id
        Integer id = userService.findUserByEmail(email).getId();

        // 2、通过id获取role集合
        List<Role> roleList = roleService.findRolesByUserId(id);

        // 3、通过role获取到用户权限
        for (int index = 0; index < roleList.size(); index++) {
            roleStringList.add(roleList.get(index).getName());
            List<Permission> permissions =  permissionService.findPermissionByRoleId(roleList.get(index).getId());
            for (int i = 0; i < permissions.size(); i++) {
                Permission permission = permissions.get(i);
                if (permission != null) {
                    permissionStringList.add(permission.getName());
                }
            }
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roleStringList);
        simpleAuthorizationInfo.addStringPermissions(permissionStringList);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得email，用于和数据库进行对比
        String email = JWTUtil.getEmail(token);
        System.out.println("token - " + token);
        System.out.println("email - " + email);
        if (email == null) {
            throw new AuthenticationException("token invalid");
        }

        User user = userService.findUserByEmail(email);
        if (email == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (! JWTUtil.verify(token, email, user.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
