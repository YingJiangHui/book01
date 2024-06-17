package org.ying.book.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.ying.book.dto.email.EmailBorrowNotificationDto;
import org.ying.book.dto.email.EmailValidationDto;
import org.ying.book.dto.user.UserDto;
import org.ying.book.enums.RoleEnum;
import org.ying.book.enums.SystemSettingsEnum;
import org.ying.book.pojo.Role;
import org.ying.book.pojo.User;
import org.ying.book.utils.GeneratorCode;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

@Service
public class EmailService {
    @Resource
    private ObjectMapper objectMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


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

    @Resource
    private SystemSettingsService systemSettingsService;

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

    public String sendVerificationEmail(EmailValidationDto emailValidationDto) throws MessagingException {
        String to = emailValidationDto.getEmail();
        String validateCode = GeneratorCode.generator(validateCodeLength);
        emailValidationDto.setCode(validateCode);
        User user = userService.getUserByEmail(emailValidationDto.getEmail());
        if(user==null){
//            读者在创建用户
            Role role = roleService.getRoleByRoleName(RoleEnum.READER);
            if(role!=null){
                emailValidationDto.setRoles(Arrays.asList(role.getRoleName()));
            }
        }else{
//            任意用户在重置密码
//        用户的话 READER 图书馆管理员 LIBRARY_ADMIN
//            emailValidationDto.setRoleIds(user.getRoles().stream().map(Role::getId).collect(Collectors.toList()));
        }

        Integer validateCodeTimeout = Integer.parseInt(systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.CAPTCHA_EXPIRE_TIME).toString());
        redisService.setKey(to, emailValidationDto, validateCodeTimeout);
        Context context = new Context();
        context.setVariable("verificationCode", validateCode);
        context.setVariable("timeout", validateCodeTimeout);
        this.sendEmail(to,"图书账号验证",context,"email-validate-code-template.html");
        return validateCode;
    }

    public String sendInvitationEmail(EmailValidationDto emailValidationDto) throws MessagingException {
        Map<String,Object> map = systemSettingsService.getSystemSettingAllMap();
        String to = emailValidationDto.getEmail();
        userService.validateRegister(UserDto.builder().email(to).build());
        String inviteCode = GeneratorCode.generator(validateCodeLength);
        emailValidationDto.setCode(inviteCode);
//        可以存一些待注册管理员的权限信息
        Integer inviteCodeTimeout = Integer.parseInt(systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.INVITATION_EXPIRE_TIME).toString());
        String inviteRegisterLink = systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.INVITATION_URL).toString();
        String link = String.format("%s?inviteCode=%s&email=%s", inviteRegisterLink,inviteCode,to);
        Context context = new Context();
        context.setVariable("link", link);
        context.setVariable("timeout", inviteCodeTimeout);
        this.sendEmail(to,"图书管理员账号申请",context,"email-invite-template.html");
        return inviteCode;
    }

    public void sendNotificationEmail(EmailBorrowNotificationDto emailBorrowNotificationDto) throws MessagingException {
        String to = emailBorrowNotificationDto.getEmail();

        Context context = new Context();
        context.setVariable("libraryName", emailBorrowNotificationDto.getLibraryName());
        context.setVariable("libraryLocationLink", emailBorrowNotificationDto.getLibraryLocationLink());
        context.setVariable("days", emailBorrowNotificationDto.getDays());
        context.setVariable("bookName", emailBorrowNotificationDto.getBookName());
        this.sendEmail(to,"取书通知",context,"email-borrow-notification.html");
    }

    public String validateEmailCode(String email, String code,String message) {
        Object codeInRedis = redisService.getValue(email);
        EmailValidationDto emailValidationDto = (EmailValidationDto) codeInRedis;
        if(emailValidationDto != null && emailValidationDto.getCode().equals(code)){
            return code;
        }
        throw new RuntimeException(message);
    }

}
