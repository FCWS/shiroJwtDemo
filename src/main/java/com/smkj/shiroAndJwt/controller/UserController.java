package com.smkj.shiroAndJwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.smkj.shiroAndJwt.bean.ResponseBean;
import com.smkj.shiroAndJwt.entiry.User;
import com.smkj.shiroAndJwt.exception.UnauthorizedException;
import com.smkj.shiroAndJwt.service.MailService;
import com.smkj.shiroAndJwt.service.UserService;
import com.smkj.shiroAndJwt.util.JWTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;

    @PostMapping("/register")
    public ResponseBean register(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();
            if (email == null)
                return new ResponseBean(200, "数据错误", "email can't null");
            if (password == null)
                return new ResponseBean(200, "数据错误", "password can't null");
            int count = 0;
            if (userService.findUserByEmail(user.getEmail()) != null) count = 1;
            System.out.println(user.getEmail() + "  " + count);
            if (count > 0) {
                return new ResponseBean(200, "该账户已经存在", user);
            }
            // 密码加密
            user.setPassword(new Sha512Hash(password).toString());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            user.setCreatedTime(df.format(new Date()));
            user.setLoginTime(df.format(new Date()));
            count = userService.insertUser(user);
            if (count < 0) {
                return new ResponseBean(200, "未知原因导致注册失败，请联系管理员", null);
            }
            // 注册成功 -> 触发邮箱激活
            mailService.send(user.getEmail(),"点击激活");
            return new ResponseBean(200, "注册成功", user);
        } catch (Exception e) {
            LOGGER.error(user.getEmail() + "用户注册失败， 失败原因：" + e.getMessage());
            return new ResponseBean(200, "注册失败", null);
        }
    }

    @PostMapping("/login")
    public ResponseBean login(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        String email = jsonObject.getString("email");
        String password = new Sha512Hash(jsonObject.getString("password")).toString();
        User user = userService.findUserByEmail(email);
        if (user == null)
            return new ResponseBean(200, "用户不存在", null);
        if (user.getPassword().equals(password)) {
            return new ResponseBean(200, "登录成功", JWTUtil.sign(email, password));
        } else {
            throw new UnauthorizedException();
        }
    }

}
