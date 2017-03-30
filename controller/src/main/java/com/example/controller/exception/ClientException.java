package com.example.controller.exception;

import android.util.Log;

import retrofit.RetrofitError;

/**
 * Created by yanghs on 2016/5/18.
 */
public class ClientException extends Exception {

    /**
     * 客户端异常的基类
     */

    public static int mExceptionCode;
    public static int CONVERSION_ERROR = 10001, NET_WORK_ERROR = 10002, HTTP_ERROR = 10003, UNEXPECTED_ERROR = 10004;

    public ClientException(RetrofitError ex) {
        if (ex.getKind().name().equals("CONVERSION")) {
            mExceptionCode = CONVERSION_ERROR; // 数据解析异常
        } else if (ex.getKind().name().equals("NETWORK")) {
            mExceptionCode = NET_WORK_ERROR; // 网络异常
        } else if (ex.getKind().name().equals("HTTP")) {
            mExceptionCode = HTTP_ERROR; // 服务器异常
        } else if (ex.getKind().name().equals("UNEXPECTED")) {
            mExceptionCode = UNEXPECTED_ERROR; // 未知异常
        }
    }


}

