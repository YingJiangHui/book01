package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.ying.book.dto.email.EmailValidationDto;
import org.ying.book.utils.GeneratorCode;

import java.io.UnsupportedEncodingException;

@SpringBootTest
@Slf4j
public class EmailServiceTest {

    @Resource
    EmailService emailService;

    @Autowired
    RedisService redisService;

    @Test
    public void sendEmailTest() throws MessagingException, UnsupportedEncodingException {
        emailService.sendVerificationCode("473380917@qq.com","99001");
    }

    @Test
    public void sendValidateCodeEmailTestUseTemplate() throws MessagingException, UnsupportedEncodingException {
        ;
        String code = emailService.sendVerificationEmail(EmailValidationDto.builder().email("15867925894@163.com").build());
        // 会在redis中存储
        Assertions.assertEquals(redisService.getValue("15867925894@163.com"),code);
    }

    @Test
    public void sendInviteCodeEmailTestUseTemplate() throws MessagingException, UnsupportedEncodingException {
        String code = emailService.sendInvitationEmail(EmailValidationDto.builder().email("15867925894@163.com").build());
        // 会在redis中存储
        Assertions.assertNotNull(redisService.getValue(code));
    }
}
