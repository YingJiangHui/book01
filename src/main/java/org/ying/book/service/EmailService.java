package org.ying.book.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.ying.book.utils.GeneratorCode;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisService redisService;

    @Value("${spring.mail.username}")
    String formEmail;

    @Value("${custom.invite-register.code-timeout}")
    int inviteCodeTimeout;

    @Value("${custom.user-validate.code-timeout}")
    int validateCodeTimeout;

    @Value("${custom.invite-register.code-length}")
    int inviteCodeLength;

    @Value("${custom.user-validate.code-length}")
    int validateCodeLength;


    @Value("${custom.invite-register.register-link}")
    String inviteRegisterLink;


    public void sendVerificationCode(String to, String verificationCode) throws MessagingException, MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(formEmail);
        helper.setTo(to);
        helper.setSubject("图书账号验证");
        helper.setText("Your verification code is: " + verificationCode);
        javaMailSender.send(mimeMessage);
    }

    public void sendEmail(String to,String title,Context context,String templateName) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(formEmail);
        helper.setTo(to);
        helper.setSubject(title);
        String htmlContent = templateEngine.process(templateName, context);
        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    public String sendVerificationEmail(String to) throws MessagingException {
        String validateCode = GeneratorCode.generator(validateCodeLength);
        redisService.setKey(to, validateCode,validateCodeTimeout);
        Context context = new Context();
        context.setVariable("verificationCode", validateCode);
        context.setVariable("timeout", validateCodeTimeout);
        this.sendEmail(to,"图书账号验证",context,"email-validate-code-template.html");
        return validateCode;
    }

    public String sendInvitationEmail(String to) throws MessagingException {
        String inviteCode = GeneratorCode.generator(validateCodeLength);
//        可以存一些待注册管理员的权限信息
        redisService.setKey(inviteCode, 1, inviteCodeTimeout);
        String link = String.format("%s?inviteCode=%s", inviteRegisterLink,inviteCode);
        Context context = new Context();
        context.setVariable("link", link);
        context.setVariable("timeout", inviteCodeTimeout);
        this.sendEmail(to,"图书管理员账号申请",context,"email-invite-template.html");
        return inviteCode;
    }
}
