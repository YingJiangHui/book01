package org.ying.book.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.enums.RoleEnum;
import org.ying.book.utils.JwtUtil;

@Slf4j
@Component
public class ReaderInterceptor implements HandlerInterceptor {
    ObjectMapper objectMapper;
    JwtUtil jwtUtil;

    @Autowired
    public void AuthInterceptor(ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();//获取请求方式
        if ("POST".equals(method)) {
            UserJwtDto userJwtDTO = UserContext.getCurrentUser();
            if (userJwtDTO.getRoles().contains(RoleEnum.READER)) {
                return true;
            } else {
                AuthInterceptor.setReturn(response, HttpServletResponse.SC_FORBIDDEN, "账号权限不足");
//           返回权限不足
                return false;
            }
        }
        return true;
    }
}