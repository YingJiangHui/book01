package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.User;
import org.ying.book.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public User getUsers(@RequestParam("id") Integer id) throws Exception {
        throw new Exception("错误");
//        return userService.getUser(id);
    }

}
