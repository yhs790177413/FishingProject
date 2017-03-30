package com.example.controller.bean;

public class BaseBean {

    public String code;
    public String message;

    public boolean ok() { //访问接口成功
        return code.equals("0") ? true : false;
    }
}
