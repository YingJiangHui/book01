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
import org.ying.book.dto.user.UserUpdateDto;
import org.ying.book.enums.RoleEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.pojo.User;
import org.ying.book.service.AuthService;
import org.ying.book.service.UserService;
import org.ying.book.utils.JwtUtil;

import java.util.HashSet;
import java.util.Set;

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
        UserJwtDto user = UserContext.getCurrentUser();
        if (user.isReaderOnly()) {
            throw new CustomException("无权限");
        }
        if (user.isLibraryAdminOnly()) {
            if (userQueryParamsDTO.getLibraryIds() == null || userQueryParamsDTO.getLibraryIds().isEmpty()) {
                throw new CustomException("无权限");
            }
            Set set = new HashSet();
            userQueryParamsDTO.getLibraryIds().forEach(id -> {
                set.add(id);
            });
            user.getManagedLibraries().forEach(id -> {
                set.add(id);
            });
            if (set.size() != user.getManagedLibraries().size()) {
                throw new CustomException("无权限");
            }
        }
        return userService.getUsersWithTotal(userQueryParamsDTO);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userService.getUser(id);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable("id") Integer id, @RequestBody UserUpdateDto userUpdateDto) {
        UserJwtDto currentUser = UserContext.getCurrentUser();
        if (currentUser.isReaderOnly()) {
            throw new CustomException("无权限");
        }
        if (currentUser.isLibraryAdminOnly()) {
            User user = userService.getUser(id);
            user.getRoles().forEach(role -> {
                if (role.getRoleName().equals(RoleEnum.SYSTEM_ADMIN)) {
                    throw new CustomException("无权限");
                }
                if (role.getRoleName().equals(RoleEnum.LIBRARY_ADMIN)) {
                    throw new CustomException("无权限");
                }
            });
        }

        return userService.updateUser(id, userUpdateDto);
    }

}
