package org.ying.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.dto.user.UserDto;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.User;
import org.ying.book.service.*;
import org.ying.book.utils.JwtUtil;
import org.ying.book.utils.Result;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Resource
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Resource
    private AuthService authService;

    @Resource
    private EmailService emailService;

    @Resource
    private EncodeService encodeService;

    @Resource
    private RedisService redisService;

    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto) {
        try {
            authService.validateRegister(userDto.getEmail(), userDto.getPassword(), userDto.getPasswordConfirmation());
            userService.validateRegister(userDto);
            emailService.validateEmailCode(userDto.getEmail(), userDto.getValidationCode(), "验证码不匹配");
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        userService.register(userDto);

        return Result.builder().build().success("注册成功");
    }

    @PostMapping("/reset-password")
    public Result resetPassword(@RequestBody UserDto userDto) {
        User user = null;
        try {
            user = userService.existUserByEmail(userDto.getEmail());
            authService.validateRegister(userDto.getEmail(), userDto.getPassword(), userDto.getPasswordConfirmation());
            emailService.validateEmailCode(userDto.getEmail(), userDto.getValidationCode(), "验证码不匹配");
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(encodeService.encode(userDto.getPassword()));
        userService.updateUserInfo(user);

        return Result.builder().build().success("密码重置成功");
    }

    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody UserDto userDto) throws JsonProcessingException {
        User userDetails = null;
        try {
            userDetails = userService.login(userDto);
        } catch (CustomException e){
            throw e;
        } catch (RuntimeException e) {
            e.getStackTrace();
            throw new CustomException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        UserJwtDto userJwtDTO = UserJwtDto.builder()
                .roles(userDetails.getRoles().stream().map((role) -> role.getRoleName()).toList())
                .email(userDetails.getEmail())
                .isBlacklist(userDetails.getIsBlacklist())
                .defaultTimes(userDetails.getDefaultTimes())
                .createdAt(userDetails.getCreatedAt())
                .id(userDetails.getId())
                .managedLibraryIds(userDetails.getLibraries().stream().map(item->item.getId()).toList())
                .build();
        Object JWT = jwtUtil.createJWT("user_auth", userJwtDTO);
        return Result.builder().build().success("登录成功", JWT);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) throws Exception {
        Object token = request.getAttribute("token");
        if(token == null){
            throw new CustomException("用户未登录", HttpStatus.UNAUTHORIZED);
        }
        redisService.setKey(token.toString(), false,jwtUtil.getExpiration(token.toString()) - new Date().getTime() , TimeUnit.MILLISECONDS);
    }
//
//    @GetMapping("/user")
//    public String getUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return "Logged in user: " + userDetails.getUsername();
//    }
}
