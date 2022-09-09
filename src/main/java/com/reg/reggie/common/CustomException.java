package com.reg.reggie.common;

/**
 * 自定义异常类
 * Created by e1hax on 2022-09-09.
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
