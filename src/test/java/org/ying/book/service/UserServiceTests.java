package org.ying.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.email.EmailValidationDto;
import org.ying.book.dto.user.UserDto;
import org.ying.book.dto.user.UserQueryParamsDTO;
import org.ying.book.enums.RoleEnum;
import org.ying.book.utils.GeneratorCode;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

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
    public void selectUserByEmailTest() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(userService.getUserByEmail("473380917@qq.com")));
    }
    @Test
    public void selectUserByRoleNameAndLibraryIdTest() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(userService.selectUserByRoleNameAndLibraryId(UserQueryParamsDTO.builder().libraryIds(Arrays.asList(1)).roleNames(Arrays.asList(RoleEnum.SYSTEM_ADMIN)).build())));
    }
    @Test
    @DisplayName("getUsers returns list of users when users exist")
    public void getUsersReturnsListOfUsersWhenUsersExist() {
        // Arrange
        PageReqDto pageReqDto = new PageReqDto();
        pageReqDto.setPageSize(30);
        pageReqDto.setCurrent(1);
//        userService.getUsers(pageReqDto).forEach(System.out::println);
    }
    @Test void registerTest() {
        String email = "4733809912@qq.com";
        String validateCode = GeneratorCode.generator(6);

        UserDto userDto = UserDto.builder().email(email).validationCode(validateCode).password("123456").passwordConfirmation("123456").build();
        EmailValidationDto emailValidationDto = EmailValidationDto.builder().roles(Arrays.asList(RoleEnum.LIBRARY_ADMIN)).email(email).code(validateCode).build();
//        emailService.sendVerificationEmail(emailValidationDto);
        redisService.setKey(email, emailValidationDto, 5);

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
