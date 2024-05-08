package org.ying.book.exception;

import org.springframework.http.HttpStatus;

public class HttpServiceErrorException extends CustomException{
    public HttpServiceErrorException(String message, Integer code) {
        super(message,code, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
