package org.ying.book.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

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

    public Result success(Object data){
        return this.success("成功",data);
    }
    public Result success(String message){
        return this.success(message,null);
    }
    public Result success(String message,Object data){
        this.code = HttpStatus.OK.value();
        this.message = message;
        this.data = data;
        return this;
    }
    public Result fail(String message){
        return this.fail(message, null);
    }
    public Result fail(Object data){
        return this.fail("失败", data);
    }
    public Result fail(String message, Object data){
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
        this.data = data;
        return this;
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
