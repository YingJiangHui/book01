package org.ying.book.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ying.book.pojo.User;
import org.ying.book.utils.JwtUtil;
import org.ying.book.utils.Result;

import java.io.IOException;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    ObjectMapper objectMapper;

    JwtUtil jwtUtil;

    @Autowired
    public AuthInterceptor(ObjectMapper objectMapper, JwtUtil jwtUtil){
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("preHandle1");
        //从header中获取token
        String authorizationHeader = request.getHeader("Authorization");//        如果token为空
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            // 如果没有提供Authorization头部或者头部格式不正确，返回401 Unauthorized错误

            setReturn(response,HttpServletResponse.SC_UNAUTHORIZED,"用户未登录，请先登录");
            return false;
        }
        // 提取token部分
        String token = authorizationHeader.substring(7); // 去掉 "Bearer " 前缀

        // 在这里可以对token进行进一步处理，比如解析JWT令牌，验证令牌的有效性等
        System.out.println(objectMapper);
        // 如果需要，你可以将token存储在request的attribute中，以便后续处理程序使用
        if(jwtUtil.isTokenExpired(token)){
            setReturn(response, HttpServletResponse.SC_UNAUTHORIZED,"认证失效请重新登录");
            return false;
        }
//        Claims JwtClaims = jwtUtil.parseJWT(token);
        //        User user = objectMapper.readValue(JwtClaims.getSubject(), User.class);
        request.setAttribute("token", token);


//        在实际使用中还会:
//         1、校验token是否能够解密出用户信息来获取访问者
//         2、token是否已经过期
        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.debug("afterCompletion");
    }

    //返回json格式错误信息
    private static void setReturn(HttpServletResponse response, Integer code, String msg) throws IOException, JsonProcessingException {
        HttpServletResponse httpResponse = response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        //UTF-8编码
        httpResponse.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(code);
        Result result = Result.builder().code(code).message(msg).success(false).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        httpResponse.getWriter().print(json);
    }
}
