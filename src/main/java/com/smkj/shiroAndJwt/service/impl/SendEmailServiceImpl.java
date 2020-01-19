package com.smkj.shiroAndJwt.service.impl;

import com.smkj.shiroAndJwt.controller.UserController;
import com.smkj.shiroAndJwt.service.SendEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmail(String to, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("尚敏科技激活邮件");
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
            LOGGER.info("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            LOGGER.error("邮件发送失败，原因：" + e.getMessage());
        }
    }
}
