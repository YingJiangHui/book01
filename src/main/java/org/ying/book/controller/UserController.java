package org.ying.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.dto.user.UserQueryParamsDTO;
import org.ying.book.pojo.User;
import org.ying.book.service.AuthService;
import org.ying.book.service.UserService;
import org.ying.book.utils.JwtUtil;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private HttpServletRequest request;
    @Resource
    private UserService userService;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    ObjectMapper objectMapper;
    @Resource
    AuthService authService;
//    @RequestMapping(method = RequestMethod.GET)
//    public User getUsers(@RequestParam("id") Integer id) throws Exception {
////        throw new Exception("错误");
////        return userService.getUser(id);
//
//        return new User();
//    }
    @GetMapping("/current")
    public UserJwtDto getUserInfo() throws Exception {
        return UserContext.getCurrentUser();
    }
//获取所有用户，支持分页
    @GetMapping
    public PageResultDto<User> getUsers(@ModelAttribute UserQueryParamsDTO userQueryParamsDTO) {
        return userService.getUsersWithTotal(userQueryParamsDTO);
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userService.getUser(id);
    }


}
