package org.ying.book.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RedisServiceTest {
    @Resource
    RedisService redisService;
//
//    @Test
//    public void setValueTest() {
//        redisService.setKey("1236","aaa", 30, TimeUnit.SECONDS);
//    }
//    @Test
//    public void getValueTest() {
//        assertEquals(redisService.getValue("1236"),"aaa");
//    }

}
