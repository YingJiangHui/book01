package org.ying.book.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

// 要加序列化注解已经getter setter 不然无法序列化
//@JsonSerialize
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private boolean success;
    //相应码
    private Integer code;
    //信息
    private String message;
    //返回数据
    private Object data;


    public Result success(Object data){
        return this.success("成功",data);
    }
    public Result success(String message){
        return this.success(message,null);
    }
    public Result success(String message,Object data){
        this.success = true;
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
        this.success = false;
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
        this.data = data;
        return this;
    }
}
