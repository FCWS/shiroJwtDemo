package com.smkj.shiroAndJwt.service;

public interface MailService {

    /**
     * 发送邮件
     * @param to 接受邮箱
     * @param text 内容
     */
    public void send(String to, String text);
}
