package com.hosigus.imageloader.Exceptions;

/**
 * Created by 某只机智 on 2018/4/6.
 * 参数缺失异常
 */

public class LackParamException extends RuntimeException {
    public LackParamException(String message) {
        super(message);
    }
}
