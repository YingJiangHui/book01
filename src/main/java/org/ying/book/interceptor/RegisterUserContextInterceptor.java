package org.ying.book.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.utils.JwtUtil;

@Component
public class RegisterUserContextInterceptor implements HandlerInterceptor {
    ObjectMapper objectMapper;

    JwtUtil jwtUtil;

    @Autowired
    public RegisterUserContextInterceptor(ObjectMapper objectMapper, JwtUtil jwtUtil){
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String authorizationHeader = request.getHeader("Authorization");//        如果token为空
            String token = authorizationHeader.substring(7); // 去掉 "Bearer " 前缀
            UserJwtDto userJwtDTO = objectMapper.readValue(jwtUtil.parseJWT(token.toString()).getSubject(), UserJwtDto.class);
            UserContext.setCurrentUser(userJwtDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
