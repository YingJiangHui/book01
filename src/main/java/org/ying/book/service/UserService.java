package org.ying.book.service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.common.PageReqDto;
import org.ying.book.dto.common.PageResultDto;
import org.ying.book.dto.email.EmailValidationDto;
import org.ying.book.dto.user.*;
import org.ying.book.enums.RoleEnum;
import org.ying.book.enums.SystemSettingsEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.UserMapper;
import org.ying.book.mapper.UserRoleMapper;
import org.ying.book.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.ying.book.pojo.UserExample;
import org.ying.book.pojo.UserRole;
import org.ying.book.utils.JwtUtil;
import org.ying.book.utils.PaginationHelper;

import java.util.*;
import java.util.concurrent.TimeUnit;


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

    @Resource
    private SystemSettingsService systemSettingsService;
    @Autowired
    private UserRoleMapper userRoleMapper;

    public List<User> getUsers(UserExample example, RowBounds rowBounds) {
        return userMapper.selectByExampleWithRowbounds(example, rowBounds);
    }

    public PageResultDto<User> getUsersWithTotal(UserQueryParamsDTO userQueryParamsDTO) {
        UserExample example = new UserExample();
        example.setDistinct(true);
        example.setOrderByClause("created_at DESC");
        UserExample.Criteria criteria = example.createCriteria();
        List<Integer> libraryIds = userQueryParamsDTO.getLibraryIds();
        List<RoleEnum> roleNames = userQueryParamsDTO.getRoleNames();
        String email = userQueryParamsDTO.getEmail();
        Boolean isBlacklist = userQueryParamsDTO.getIsBlacklist();
        if (userQueryParamsDTO.getId() != null) {
            criteria.andIdEqualTo(userQueryParamsDTO.getId());
        }
        if (libraryIds != null && !libraryIds.isEmpty()) {
            criteria.andLibraryIdIn(libraryIds);
        }
        if (roleNames != null && !roleNames.isEmpty()) {
            criteria.andRoleIn(roleNames);
        }
        if (isBlacklist != null) {
            criteria.andIsBlacklistEqualTo(isBlacklist);
        }
        if (email != null && !email.isEmpty()) {
            criteria.andEmailEqualTo(email.trim());
        }

        return PaginationHelper.paginate(userQueryParamsDTO, (rowBounds, reqDto) -> userMapper.selectByExampleWithRoleNameAndLibraryAndRowbounds(example, rowBounds), userMapper.countByExampleWithRoleAndLibrary(example));
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
            throw new CustomException("用户不存在", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public void validateRegister(UserDto userDto) {
        User user = getUserByEmail(userDto.getEmail());
        if (user != null) {
            throw new CustomException("邮箱已存在", HttpStatus.BAD_REQUEST);
        }
    }

    public User login(UserDto userDto) {
        existUserByEmail(userDto.getEmail());
        User user = getUserByEmail(userDto.getEmail());
        if (user.getPassword().equals(encodeService.encode(userDto.getPassword()))) {
            log.info("密码校验通过");
            return user;
        }
        throw new CustomException("密码错误");
    }

    @Transactional
    public void register(UserDto userDto) {
        // Get validation code
        Object codeInRedis = redisService.getValue(userDto.getEmail());
        EmailValidationDto emailValidationDto = (EmailValidationDto) codeInRedis;
        //      Create User Entity
        User user = User.builder().email(userDto.getEmail()).password(encodeService.encode(userDto.getPassword())).build();
        //      Insert User
        userMapper.insertSelective(user);
        //      Bind userRoles
        if (Optional.ofNullable(emailValidationDto.getRoles()).isPresent()) {
            roleService.userRelativeRoles(user.getId(), emailValidationDto.getRoles());
        } else {
            throw new RuntimeException("没有提供用户权限信息");
        }
        //      Bind library
        libraryService.userRelativeLibraries(user.getId(), emailValidationDto.getLibraryIds());
    }

    public void updateUserInfo(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    public List<User> selectUserByRoleNameAndLibraryId(UserQueryParamsDTO userQueryParamsDTO) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andLibraryIdIn(userQueryParamsDTO.getLibraryIds()).andRoleIn(userQueryParamsDTO.getRoleNames());
        return userMapper.selectByExampleWithRoleNameAndLibraryAndRowbounds(example, new RowBounds());
    }


    public void defaultTimesAddOne(Integer userId) {
        Integer maxDefaultTimes = Integer.parseInt(systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.MAX_OVERDUE_TIMES).toString());
        User user = userMapper.selectByPrimaryKey(userId);
        user.setDefaultTimes(user.getDefaultTimes() + 1);
        if (user.getDefaultTimes() > maxDefaultTimes) {
            user.setIsBlacklist(true);
        }
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Transactional
    public User updateUser(Integer userId, UserUpdateDto userUpdateDto) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new CustomException("用户不存在", HttpStatus.NOT_FOUND);
        }
        if (userUpdateDto.getIsBlacklist() != null) {
            user.setIsBlacklist(userUpdateDto.getIsBlacklist());
        }
        roleService.userRelativeRoles(userId, userUpdateDto.getRoles());
        userMapper.updateByPrimaryKeySelective(user);
        //      Bind library
        libraryService.userRelativeLibraries(user.getId(), userUpdateDto.getLibraryIds());

        return user;
    }

    public String checkUserIsLogout(String email, String token) {
        Object object = redisService.getValue("logout" + email);
        UserLogoutDto userLogoutDto = (UserLogoutDto) object;
//        当被注销用户的请求到达
        if (userLogoutDto!=null && userLogoutDto.getInfoChanged()) {
            deleteLogoutInfo(email);
            userLogoutDto.setInfoChanged(false);
            userLogoutDto.setToken(token);
            logout(userLogoutDto);
            return userLogoutDto.getMessage();
        }

        Object tokenObj = redisService.getValue(token);
        if(tokenObj!=null){
            return tokenObj.toString();
        }

        return null;
    }

    public void disableUserToken(String token, String message) {
        try {
            redisService.setKey(token, message, jwtUtil.getExpiration(token) - new Date().getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String checkUserTokenIsDisable(String token) {
        Object object = redisService.getValue(token);
        return object != null ? (String) object : null;
    }

    public void deleteLogoutInfo(String email) {
        redisService.deleteKey("logout" + email);
    }

    public void logout( UserLogoutDto userLogoutDto) {
        try {
            if (userLogoutDto.getInfoChanged()!=null && userLogoutDto.getInfoChanged()) {
                Integer expiration = Integer.parseInt(systemSettingsService.getSystemSettingValueByName(SystemSettingsEnum.TOKEN_EXPIRATION).toString());
                redisService.setKey("logout" + userLogoutDto.getEmail(), userLogoutDto, expiration * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
            } else {
                disableUserToken(userLogoutDto.getToken().toString(), userLogoutDto.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
