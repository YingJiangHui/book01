package org.ying.book.service;

import jakarta.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.ying.book.dto.UserDto;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.UserMapper;
import org.ying.book.pojo.User;
import org.ying.book.pojo.UserExample;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Resource
    private EncodeService encodeService;

    @Resource
    private UserMapper userMapper;

    User getUserByEmail(String email){
        UserExample userPOExample = new UserExample();
        userPOExample.createCriteria().andUsernameEqualTo(email);

        return userMapper.selectByExample(userPOExample).get(0);
    }

    public void validateRegister(UserDto userDto){
        User user = getUserByEmail(userDto.getEmail());
        if(user != null) {
             throw new RuntimeException("邮箱已存在");
        }
    }

    public User getUser(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public User login(UserDto userDto){
        User user = getUserByEmail(userDto.getEmail());
        if( user.getPassword().equals(encodeService.encode(userDto.getPassword()))){
            log.info("密码校验通过");
            return user;
        }
        throw new CustomException("密码错误", HttpStatus.UNAUTHORIZED);
    }
    public void logout(){

    }
    public void register(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(encodeService.encode(userDto.getPassword()));
        userMapper.insertSelective(user);
    }

}