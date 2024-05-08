package org.ying.book.service;

import org.springframework.stereotype.Service;
import org.ying.book.dto.UserDto;

@Service
public class AuthService {
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
