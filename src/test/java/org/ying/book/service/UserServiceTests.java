package org.ying.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class UserServiceTests {
    @Resource
    ObjectMapper objectMapper;
    @Resource
    private UserService userService;
    @Test
    public void test() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(userService.getUserByEmail("473380917@qq.com")));
    }

}
