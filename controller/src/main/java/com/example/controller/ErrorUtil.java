package com.example.controller;

/**
 * Created by yanghs on 2016/5/18.
 */
public class ErrorUtil {

    public static String getNetworkError() {
        return "网络异常，请检查你的网络";
    }

    public static String getServerError(String msg) {
        return msg;
    }

    public static String getHttpError() {
        return "访问服务器出现异常，请确认客户端是否为最新版本后重试。\n如果您使用最新版本重试后依然出现这个提示，请及时联系我们，谢谢！";
    }

    public static String getConversionError() {
        return "数据解析出现异常，如果您使用最新版本重试后依然出现这个提示，请及时联系我们，谢谢！";
    }

    public static String getUnExceptionError() {
        return "未知异常，如果您使用最新版本重试后依然出现这个提示，请及时联系我们，谢谢！";
    }
}
