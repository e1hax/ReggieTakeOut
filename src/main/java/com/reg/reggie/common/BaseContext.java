package com.reg.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 基于ThreadLocal封装的工具类，用户保存和获取用户的session
 * Created by e1hax on 2022-09-09.
 */
public class BaseContext{
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置用户id
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取用户id
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
