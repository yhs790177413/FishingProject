package com.example.controller.controller;
/**
 * Created by yanghs on 2016/5/18.
 */
public interface Listener<T> {
    public void onStart(Object... params);

    public void onComplete(T result, Object... params);

    public void onFail(String msg, Object... params);
}
