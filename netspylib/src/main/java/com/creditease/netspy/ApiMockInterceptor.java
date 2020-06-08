package com.creditease.netspy;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhxh on 2020/6/8
 * 为
 */
public final class ApiMockInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!ApiMockHelper.isApiMock) {//默认false 不拦截
            Request request = chain.request();
            return chain.proceed(request);
        }

        Request request = chain.request();


        return null;
    }
}
