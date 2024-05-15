package org.ying.book.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.ying.book.dto.user.UserDto;
import org.ying.book.mapper.UserMapper;
import org.ying.book.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.ying.book.utils.JwtUtil;


@Service
@Slf4j
public class UserService {

    @Resource
    private EncodeService encodeService;

    @Resource
    private UserMapper userMapper;

    @Resource
    ObjectMapper objectMapper;

    @Resource
    JwtUtil jwtUtil;

    User getUserByEmail(String email){
        return userMapper.selectUserByEmail(email);
    }

    public User existUserByEmail(String email){
        User user = getUserByEmail(email);
        if(user == null){
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    public void validateRegister(UserDto userDto){
        User user = getUserByEmail(userDto.getEmail());
        if(user != null) {
             throw new RuntimeException("邮箱已存在");
        }
    }

    public User login(UserDto userDto){
        existUserByEmail(userDto.getEmail());
        User user = getUserByEmail(userDto.getEmail());
        if( user.getPassword().equals(encodeService.encode(userDto.getPassword()))){
            log.info("密码校验通过");
            return user;
        }
        throw new RuntimeException("密码错误");
    }

    public void register(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(encodeService.encode(userDto.getPassword()));
        userMapper.insertSelective(user);
    }

    public void updateUserInfo(User user){
        userMapper.updateByPrimaryKey(user);
    }
}
