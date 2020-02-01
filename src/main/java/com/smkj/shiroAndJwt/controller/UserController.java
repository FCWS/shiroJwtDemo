package com.smkj.shiroAndJwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.smkj.shiroAndJwt.bean.ResponseBean;
import com.smkj.shiroAndJwt.dao.RoleMapper;
import com.smkj.shiroAndJwt.entiry.User;
import com.smkj.shiroAndJwt.exception.UnauthorizedException;
import com.smkj.shiroAndJwt.service.SendEmailService;
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
import java.util.Random;

@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private SendEmailService sendEmailService;

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
            System.out.println(count);
            if (count == 1) {
                return new ResponseBean(200, "该账户已经存在", user);
            }
            // 密码加密
            String code = getCode();
            user.setCode(code);
            user.setPassword(new Sha512Hash(password).toString());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            user.setCreatedTime(df.format(new Date()));
            user.setLoginTime(df.format(new Date()));
            System.out.println(user.toString());
            userService.insertUser(user);
            int id = user.getId();
            if (id == 0) {
                return new ResponseBean(200, "未知原因导致注册失败，请联系管理员", null);
            }
            System.out.println("插入id为：" + id);
            // 触发邮箱激活
            String activateUrl = "<a href=\"http://localhost:8080/activate?code="+code+"\">点我激活，有效时间5分钟</a>";
            sendEmailService.sendEmail(user.getEmail(), activateUrl);
            // 赋予普通用户权限
            count = roleMapper.insertDefaultRole(id);
            if (count > 0) {
                return new ResponseBean(200, "注册成功", user);
            } else {
                return new ResponseBean(200, "初始化权限失败，请联系管理员", user);
            }

        } catch (Exception e) {
            LOGGER.error(user.getEmail() + " 用户注册失败， 失败原因：" + e.getMessage());
            return new ResponseBean(200, "注册失败", null);
        }
    }

    @GetMapping("/activate")
    public ResponseBean activate(@RequestParam String code, HttpServletResponse request, HttpServletResponse response) {
        try {
            if (code != null) {
                // 根据code获取用户信息 判断验证码是否过期
                User user = userService.findUserByCode(code);
                if (user == null)
                    return new ResponseBean(200, "无效激活码", null);
                if (user.getActivity() == 1)
                    return new ResponseBean(200, "不能重复激活", null);
                String registerTime = user.getCreatedTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String currentTime = df.format(new Date().getTime() - 5 * 60 * 1000);
                if (df.parse(currentTime).compareTo(df.parse(registerTime)) > 0)
                    return new ResponseBean(200, "激活码超时", null);
                int count = userService.activateWithCode(code);
                if (count > 0) {
                    user.setActivity(1);
                    return new ResponseBean(200, "激活成功", user);
                } else {
                    return new ResponseBean(200, "激活失败", null);
                }
            } else {
                return new ResponseBean(200, "激活失败", null);
            }
        } catch (Exception e) {
            return new ResponseBean(200, e.getMessage(), null);
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
        if (user.getActivity() == 0)
            return new ResponseBean(200, "尚未激活，先激活", null);
        if (user.getPassword().equals(password)) {
            String token = JWTUtil.sign(email, password);
            response.setHeader("authorization", token);
            // 注意：前端默认只能获取到默认返回头信息， 为了支持正常获取到authorization 必须设置下面的属性
            response.setHeader("Access-Control-Expose-Headers", "authorization");
            return new ResponseBean(200, "登录成功", token);
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * 重新发送激活码
     * 只有登录用户 才能触发重新发送激活码
     * desc: 激活码有效时间5分钟
     * @param email
     * @param request
     * @param response
     * @return
     */
    @PostMapping("reSendCode")
    public ResponseBean reSendCode(@RequestParam String email, HttpServletRequest request, HttpServletResponse response) {
        // 1、发送激活码
        User user = userService.findUserByEmail(email);
        if (user == null)
            return new ResponseBean(200, "用户不存在", null);
        if (user.getActivity() == 1)
            return new ResponseBean(200, "账户已经激活", user);
        String code = getCode();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        int count = userService.updateCodeByEmail(email, code, df.format(new Date()));
        if (count < 0)
            return new ResponseBean(200, "激活码发送失败", null);
        String activateUrl = "<a href=\"http://localhost:8080/activate?code="+code+"\">点我激活，有效时间5分钟</a>";
        sendEmailService.sendEmail(user.getEmail(), activateUrl);
        return new ResponseBean(200, "激活码发送成功", null);
    }

    /**
     * 随机生成激活码
     * @return
     */
    private String getCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 8; i++) {
            int temp = random.nextInt(50);
            char x = (char)(temp < 26 ? temp + 97 : (temp % 26) + 65);
            code += x;
        }
        return code;
    }

}
