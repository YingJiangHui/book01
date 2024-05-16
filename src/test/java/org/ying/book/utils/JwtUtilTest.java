package org.ying.book.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.ying.book.pojo.User;

import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class JwtUtilTest {
    @Resource
    JwtUtil jwtUtil;
    @Resource
    ObjectMapper objectMapper;
    @Test
    void JwtKeyGeneratorTest() throws NoSuchAlgorithmException {
        SecretKeySpec key = jwtUtil.generalKey();
        assertNotNull(key);
        assertTrue(key.toString().length() > 0);
    }
    @Test
    void generationJwtToken() throws Exception {
        User user = User.builder().build();
        user.setUsername("weiyibiaoshi");
        user.setEmail("47");
        String json = objectMapper.writeValueAsString(user);
        String jwtToken = jwtUtil.createJWT("weiyibiaoshi", json, 36000);
        assertNotNull(jwtToken);
        assertTrue(jwtToken.length() > 0);
    }

    @Test
    void generationJwtTokenParse() throws Exception {
        User user = User.builder().build();
        user.setUsername("weiyibiaoshi");
        user.setEmail("47");
        String json = objectMapper.writeValueAsString(user);
        String jwtToken = jwtUtil.createJWT("weiyibiaoshi", json, 36000);
        Claims parsedInfo = jwtUtil.parseJWT(jwtToken);
        User user2 =  objectMapper.readValue(json, User.class);
        assertNotNull(parsedInfo);
        assertTrue(parsedInfo.getId().equals("weiyibiaoshi"));
        assertTrue(user2.getUsername().equals(user.getUsername()));
        assertTrue(user2.getEmail().equals(user.getEmail()));
    }
    @Test
    void JwtTokenIs() throws Exception {
        User user = User.builder().build();
        user.setUsername("weiyibiaoshi");
        user.setEmail("47");
        String json = objectMapper.writeValueAsString(user);
        String jwtToken0 = jwtUtil.createJWT("weiyibiaoshi", json, 36000);
        boolean tokenIsExpired0 = jwtUtil.isTokenExpired(jwtToken0);
        assertFalse(tokenIsExpired0);
        String jwtToken1 = jwtUtil.createJWT("weiyibiaoshi", json, 0);
        boolean tokenIsExpired1 = jwtUtil.isTokenExpired(jwtToken1);
        assertTrue(tokenIsExpired1);

    }

}
