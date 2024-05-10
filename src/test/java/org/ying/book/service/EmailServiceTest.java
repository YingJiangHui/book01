package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.ying.book.utils.GeneratorCode;

import java.io.UnsupportedEncodingException;

@SpringBootTest
@Slf4j
public class EmailServiceTest {

    @Resource
    EmailService emailService;

    @Value("${custom.invite-register-link}")
    String inviteRegisterLink;

    @Test
    public void sendEmailTest() throws MessagingException, UnsupportedEncodingException {
        emailService.sendVerificationCode("473380917@qq.com","99001");
    }

    @Test
    public void sendValidateCodeEmailTestUseTemplate() throws MessagingException, UnsupportedEncodingException {
        String validateCode = GeneratorCode.generator(6);
        emailService.sendVerificationEmail("15867925894@163.com","jh",validateCode);
    }

    @Test
    public void sendInviteCodeEmailTestUseTemplate() throws MessagingException, UnsupportedEncodingException {
        String link = String.format("%s?inviteCode=%s", inviteRegisterLink,GeneratorCode.generator(8));
        emailService.sendInvitationEmail("15867925894@163.com",link);
    }
}
