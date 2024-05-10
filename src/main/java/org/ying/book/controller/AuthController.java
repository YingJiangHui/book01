package org.ying.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ying.book.dto.user.UserDto;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.User;
import org.ying.book.service.AuthService;
import org.ying.book.service.UserService;
import org.ying.book.utils.JwtUtil;
import org.ying.book.utils.Result;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Resource
    private AuthService authService;

    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto) {
        try {
            authService.validateRegister(userDto.getEmail(),userDto.getPassword(),userDto.getPasswordConfirmation());
            userService.validateRegister(userDto);
        }catch (RuntimeException e){
            e.getStackTrace();
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        userService.register(userDto);
        return new Result().success("注册成功");
    }

    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody UserDto userDto) throws JsonProcessingException {
        User userDetails = userService.login(userDto);
        Object JWT  = jwtUtil.createJWT("user_auth",userDetails);
        return new Result().success("登录成功",JWT);
    }

//    @PostMapping("/logout")
//    public String logout(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        session.invalidate();
//        return "Logout successful";
//    }
//
//    @GetMapping("/user")
//    public String getUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return "Logged in user: " + userDetails.getUsername();
//    }
}
