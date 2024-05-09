package org.ying.book.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ying.book.dto.user.UserDto;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.User;
import org.ying.book.service.AuthService;
import org.ying.book.service.UserService;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Resource
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto) {
        try {
            authService.validateRegister(userDto.getEmail(),userDto.getPassword(),userDto.getPasswordConfirmation());
            userService.validateRegister(userDto);
        }catch (RuntimeException e){
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        userService.register(userDto);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, @RequestBody UserDto userDto) {
        HttpSession session = request.getSession();
        User userDetails = userService.login(userDto);
        session.setAttribute("user", userDetails);
        return "Login successful";
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
