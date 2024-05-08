package org.ying.book.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

// 要加序列化注解已经getter setter 不然无法序列化
@JsonSerialize
public class Result {
    //相应码
    private Integer code;
    //信息
    private String message;
    //返回数据
    private Object data;
    public Result() {}

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
