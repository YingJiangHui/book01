package org.ying.book.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Result> handleCustomException(JsonProcessingException ex) {
        Result result = new Result();
        result.setCode(HttpStatus.SERVICE_UNAVAILABLE.value());
        result.setMessage("JSON 解析失败：" + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.SERVICE_UNAVAILABLE);
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