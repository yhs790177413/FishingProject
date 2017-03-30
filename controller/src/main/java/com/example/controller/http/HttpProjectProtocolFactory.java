package com.example.controller.http;

import com.example.controller.BaseApplication;
import com.example.controller.exception.ClientException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.OkClient;

@SuppressWarnings("unchecked")
/**
 * 封装一个https协议工厂
 */
public class HttpProjectProtocolFactory {

    // 接口域名
    public static final String HOST_URL_P = "https://nardiaoyu.com/";

    private static final Map<String, Object> sHttpProtocolMap = new ConcurrentHashMap<String, Object>();


    public static <T> T getProtocol(String host, Class<T> clazz) {
        if (sHttpProtocolMap.containsKey(clazz.getName())) {
            return (T) sHttpProtocolMap.get(clazz.getName());
        }

        Client client = new OkClient();
        //通过RestAdapter生成一个接口的实现类，使用的是动态代理
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(host)//设置api
                .setErrorHandler(new MyErrorHandler())//设置自定义异常处理
                .setRequestInterceptor(new MyRequestInterceptor())
                .setLogLevel(
                        BaseApplication.DEBUG ? RestAdapter.LogLevel.FULL
                                : RestAdapter.LogLevel.NONE).build();//设置是否打印retrofit的日志
        T t = restAdapter.create(clazz);
        sHttpProtocolMap.put(clazz.getName(), t);
        return t;
    }

    private static class MyErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            return new ClientException(cause);
        }
    }

    /**
     * 一般用来设置UA、设置缓存策略 、打印Log 等
     */
    private static class MyRequestInterceptor implements RequestInterceptor {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("User-Agent", "Client-Android");
            request.addHeader("ostype", "2");
            request.addHeader("os", "android");
        }
    }
}
