package org.ying.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.ying.book.dto.email.EmailValidationDto;
import org.ying.book.dto.user.UserDto;
import org.ying.book.utils.GeneratorCode;
import org.ying.book.utils.Result;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Resource
    ObjectMapper objectMapper;
    @Resource
    private UserService userService;
    @Resource
    private EmailService emailService;
    @Autowired
    private AuthService authService;

    @Autowired
    private RedisService redisService;

    @Test
    public void test() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(userService.getUserByEmail("473380917@qq.com")));
    }

    @Test void registerTest() {

        String validateCode = GeneratorCode.generator(6);

        UserDto userDto = UserDto.builder().email("473380991@qq.com").validationCode(validateCode).password("123456").passwordConfirmation("123456").build();
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(1);
        EmailValidationDto emailValidationDto = EmailValidationDto.builder().roleIds(roleIds).email("473380991@qq.com").code(validateCode).build();
//        emailService.sendVerificationEmail(emailValidationDto);
        redisService.setKey("473380991@qq.com", emailValidationDto, 5);

        userService.register(userDto);
//        userService.register(userDto);
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> request = new HttpEntity<>(userDtoJson, headers);
//        ResponseEntity<Result> response = restTemplate.postForEntity("http://localhost:8099/api/auth/register", request, Result.class);
//        response.getBody();
//        log.info(response.getBody().toString());
    }

}
