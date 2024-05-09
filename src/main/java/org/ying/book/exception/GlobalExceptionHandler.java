package org.ying.book.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.ying.book.utils.Result;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ModelAttribute()
    public Map<String, Object> globalModelAttributes() {
        // 返回一个Map，包含统一的数据结构
        Map<String, Object> response = new HashMap<>();
        response.put("data", null); // 初始化data字段为空数组
        response.put("code", 200); // 设置默认的状态码为200
        response.put("message", "ok"); // 设置默认的消息为ok
        return response;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result> handleCustomException(CustomException ex) {
        Result result = new Result();
        result.setCode(ex.getCode());
        result.setMessage(ex.getMessage());
        return new ResponseEntity<>(result, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception ex) {
        log.debug("全局捕获的未知异常：" + ex.toString());
        Result result = new Result();
        result.setMessage("服务器异常：" + ex);
        result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}