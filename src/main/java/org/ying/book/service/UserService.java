package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.email.EmailValidationDto;
import org.ying.book.dto.user.UserDto;
import org.ying.book.mapper.UserMapper;
import org.ying.book.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.ying.book.pojo.UserExample;
import org.ying.book.utils.JwtUtil;
import org.ying.book.utils.PaginationHelper;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserService {

    @Resource
    private RedisService redisService;

    @Resource
    private EncodeService encodeService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleService roleService;

    @Resource
    LibraryService libraryService;

    @Resource
    JwtUtil jwtUtil;

    public List<User> getUsers(UserExample example,RowBounds rowBounds) {
        return userMapper.selectByExampleWithRowbounds(example, rowBounds);
    }
    public PageResultDto<User> getUsersWithTotal(PageReqDto pageReqDto) {
        UserExample example = new UserExample();
        return PaginationHelper.paginate(pageReqDto, (rowBounds, reqDto) -> this.getUsers(example, rowBounds), userMapper.countByExample(example));
    }
    public User getUser(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    User getUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    public User existUserByEmail(String email) {
        User user = getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    public void validateRegister(UserDto userDto) {
        User user = getUserByEmail(userDto.getEmail());
        if (user != null) {
            throw new RuntimeException("邮箱已存在");
        }
    }

    public User login(UserDto userDto) {
        existUserByEmail(userDto.getEmail());
        User user = getUserByEmail(userDto.getEmail());
        if (user.getPassword().equals(encodeService.encode(userDto.getPassword()))) {
            log.info("密码校验通过");
            return user;
        }
        throw new RuntimeException("密码错误");
    }

    @Transactional
    public void register(UserDto userDto) {
        Object codeInRedis = redisService.getValue(userDto.getEmail());
        EmailValidationDto emailValidationDto = (EmailValidationDto) codeInRedis;
        //      Create User Entity
        User user = User.builder().email(userDto.getEmail()).password(encodeService.encode(userDto.getPassword())).build();
        //      Insert User
        userMapper.insertSelective(user);
        //      Bind userRoles
        if(Optional.ofNullable(emailValidationDto.getRoles()).isPresent()){
            roleService.userRelativeRoles(user.getId(),emailValidationDto.getRoles());
        }else{
            throw new RuntimeException("没有提供用户权限信息");
        }
        //      Bind library
        libraryService.userRelativeLibraries(user.getId(),emailValidationDto.getLibraryIds());
    }

    public void updateUserInfo(User user) {
        userMapper.updateByPrimaryKey(user);
    }
}
