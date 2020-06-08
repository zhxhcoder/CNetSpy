package com.creditease.netspy;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
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

        String reg = "http.+\\.com(?=/)";

        Request request = chain.request();

        //获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        HttpUrl oldHttpUrl = request.url();

        Log.d("xxxxxxxxx-old", oldHttpUrl.toString());
        Log.d("xxxxxxxxx-old", oldHttpUrl.scheme());
        Log.d("xxxxxxxxx-old", oldHttpUrl.host());
        Log.d("xxxxxxxxx-old", "" + oldHttpUrl.port());

        HttpUrl newHttpUrl = oldHttpUrl
                .newBuilder()
                .scheme("http")
                .host(ApiMockHelper.baseUrl)
                .port(5000)
                .build();

        Log.d("xxxxxxxxx-new", newHttpUrl.toString());
        Log.d("xxxxxxxxx-new", newHttpUrl.scheme());
        Log.d("xxxxxxxxx-new", newHttpUrl.host());
        Log.d("xxxxxxxxx-new", "" + newHttpUrl.port());

        return chain.proceed(builder.url(newHttpUrl).build());
    }
}
