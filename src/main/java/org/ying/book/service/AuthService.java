package org.ying.book.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.ying.book.pojo.User;
import org.ying.book.utils.JwtUtil;

@Service
public class AuthService {
    @Resource
    ObjectMapper objectMapper;

    @Resource
    JwtUtil jwtUtil;

    public <T> T parseJWT(String token,Class<T> Clazz) throws Exception {
        return objectMapper.readValue(jwtUtil.parseJWT(token.toString()).getSubject(), Clazz);
    }

    public void validateRegister(String email, String password, String confirmPassword){
        validateEmail(email);
        validatePassword(password,confirmPassword);
    }

    public void validateEmail(String email) {
        // 简单校验邮箱格式
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new RuntimeException("请输入正确的邮箱格式");
        }
    }

    public void validatePassword(String password,String confirmPassword) {
        // 校验密码长度
        if (password.length() < 6 || password.length() > 18) {
            throw new RuntimeException("密码长度在6~18为之间");
        }

        if(!password.equals(confirmPassword)){
            throw new RuntimeException("输入两次密码不相同");
        }
    }
}
