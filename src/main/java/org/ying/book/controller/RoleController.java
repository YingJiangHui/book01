package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ying.book.pojo.Role;
import org.ying.book.service.RoleService;

import java.util.List;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {
    @Resource
    private RoleService roleService;
    @GetMapping()
    public List<Role> getRoles(){
        return roleService.getRoles();
    }
}
