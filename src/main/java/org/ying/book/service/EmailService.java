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

    @Value("${spring.mail.username}")
    String formEmail;

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

    public void sendVerificationEmail(String to, String name, String verificationCode) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verificationCode", verificationCode);
        this.sendEmail(to,"图书账号验证",context,"email-validate-code-template.html");
    }

    public void sendInvitationEmail(String to, String link) throws MessagingException {
        Context context = new Context();
        context.setVariable("link", link);
        this.sendEmail(to,"图书管理员账号申请",context,"email-invite-template.html");
    }
}
