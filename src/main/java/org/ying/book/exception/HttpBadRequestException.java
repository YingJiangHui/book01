package org.ying.book.exception;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;

public class HttpBadRequestException extends CustomException{
    public HttpBadRequestException(String message, Integer code,  HttpStatus status) {
        super(message,code, HttpStatus.BAD_REQUEST);
    }
}
