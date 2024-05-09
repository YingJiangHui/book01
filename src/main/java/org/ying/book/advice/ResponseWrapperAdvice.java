package org.ying.book.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.ying.book.utils.Result;

/**
 * 自动包装返回结果
 */
@ControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 只处理带有@RestController注解的Controller方法
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 在返回内容之前对内容进行包装
        // 这里假设返回的内容是一个Map
        if (body instanceof ResponseEntity) {
            // 如果返回的是ResponseEntity，则直接返回，不需要包装
            return body;
        } else if (body instanceof String) {
            // 如果返回的是字符串，则需要手动包装
            // 这里假设返回的是一个错误消息字符串
            return new ResponseEntity<>(new Result(HttpStatus.OK.value(),"ok",body), HttpStatus.OK);
        } else {
            // 其他情况，将返回内容包装在ResponseWrapper中
            return new Result(HttpStatus.OK.value(),"ok",body);
        }
    }
}
