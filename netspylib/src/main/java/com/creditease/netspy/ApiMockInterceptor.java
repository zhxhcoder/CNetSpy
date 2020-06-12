package com.creditease.netspy;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhxh on 2020/6/8
 * 为了模拟采取的措施
 */
public final class ApiMockInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            return chain.proceed(request);
        //还要处理同一个接口 删除不相干参数 或者 统一加上一个特殊的参数

    }
}
