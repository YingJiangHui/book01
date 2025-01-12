package org.ying.book.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.ying.book.Context.UserContext;
import org.ying.book.dto.user.UserJwtDto;
import org.ying.book.dto.user.UserLogoutDto;
import org.ying.book.service.RedisService;
import org.ying.book.service.UserService;
import org.ying.book.utils.JwtUtil;
import org.ying.book.utils.Result;

import java.io.IOException;


@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UserService userService;
    ObjectMapper objectMapper;

    JwtUtil jwtUtil;

    RedisService redisService;

    @Autowired
    public AuthInterceptor(ObjectMapper objectMapper, JwtUtil jwtUtil, RedisService redisService, UserService userService){
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.userService = userService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
        // 如果需要，你可以将token存储在request的attribute中，以便后续处理程序使用
        if(jwtUtil.isTokenExpired(token)){
            setReturn(response, HttpServletResponse.SC_UNAUTHORIZED,"用户认证过期请重新登录");
            return false;
        }
        UserJwtDto userJwtDTO = objectMapper.readValue(jwtUtil.parseJWT(token).getSubject(), UserJwtDto.class);
        String message = userService.checkUserIsLogout(userJwtDTO.getEmail(), token);
        if(message != null){
            setReturn(response, HttpServletResponse.SC_UNAUTHORIZED,message);
            return false;
        }
        request.setAttribute("token",token);



        if(userJwtDTO.isBlacklist()){
            setReturn(response, HttpServletResponse.SC_FORBIDDEN,"用户已禁用，请联系管理员");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.debug("afterCompletion");
    }

    //返回json格式错误信息
    public static void setReturn(HttpServletResponse response, Integer code, String msg) throws IOException, JsonProcessingException {
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
