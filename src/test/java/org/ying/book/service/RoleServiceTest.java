package org.ying.book.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.ying.book.enums.RoleEnum;

@SpringBootTest
public class RoleServiceTest {
    @Resource
    private RoleService roleService;

    @Test
    public void test() {
        System.out.println(roleService.getRoleByRoleName(RoleEnum.SYSTEM_ADMIN).getId());
    }
}
