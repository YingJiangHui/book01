package org.ying.book.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.user.UserDto;
import org.ying.book.enums.RoleEnum;
import org.ying.book.exception.CustomException;
import org.ying.book.mapper.RoleMapper;
import org.ying.book.mapper.UserRoleMapper;
import org.ying.book.pojo.Role;
import org.ying.book.pojo.RoleExample;
import org.ying.book.pojo.UserRole;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class RoleService {
    @Resource
    private RoleMapper roleMapper;


    @Resource
    private UserRoleMapper userRoleMapper;

    @Transactional
    public void userRelativeRoles(Integer userId, List<RoleEnum> roles) {
        Optional.ofNullable(roles).ifPresent(ids -> {
            if (ids.stream().map(roleName -> roleMapper.selectByRoleName(roleName)).anyMatch(Objects::isNull)) {
                throw new CustomException("角色不存在", HttpStatus.BAD_REQUEST);
            }
            ids.stream()
                    .map(roleName -> UserRole.builder().userId(userId).roleId(roleMapper.selectByRoleName(roleName).getId()).build())
                    .forEach(userRole -> userRoleMapper.insertSelective(userRole));
        });
    }

    public Role getRoleByRoleName(RoleEnum roleName) {
//        RoleExample roleExample = new RoleExample();
//        RoleExample.Criteria criteria = roleExample.createCriteria();
//        criteria.andRoleNameEqualTo(roleName);
//        List<Role> roles = roleMapper.selectByExample(roleExample);
//        return roleMapper.selectByExample(roleExample).get(0);
        return roleMapper.selectByRoleName(roleName);
    }

    public List<Role> getRoles() {
        RoleExample example = new RoleExample();
        return roleMapper.selectByExample(example);
    }

    public Role getRoleById(int id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public void insertRole(Role role) {
        roleMapper.insert(role);
    }

    @Transactional
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Transactional
    public void deleteRole(int id) {
        roleMapper.deleteByPrimaryKey(id);
    }
}
