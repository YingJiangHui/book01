package org.ying.book.exception;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {


    private Integer code;
    private HttpStatus status;


    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public CustomException(String message, Integer code, HttpStatus status) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.code = status.value();
    }

    public CustomException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = status.value();
    }

    public HttpStatus getStatus() {
        return status;
    }
}
