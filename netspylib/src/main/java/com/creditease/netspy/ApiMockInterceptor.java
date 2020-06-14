package com.creditease.netspy;

import java.io.IOException;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
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
        if (!ApiMockHelper.isApiMock) {//默认false 不拦截
            Request request = chain.request();
            return chain.proceed(request);
        }

        //还要处理同一个接口 删除不相干参数 或者 统一加上一个特殊的参数
        Request request = chain.request();

        StringBuilder pathParams = new StringBuilder();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();

        if ("GET".equals(request.method())) { // GET方法
            HttpUrl httpUrl = urlBuilder.build();
            Set<String> paramKeys = httpUrl.queryParameterNames();

            for (String param : ApiMockHelper.paramSet) {
                if (paramKeys.contains(param)) {
                    pathParams.append("--")
                            .append(param)
                            .append("--")
                            .append(httpUrl.queryParameter(param));
                }
            }

        } else {
            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();

                for (int i = 0; i < formBody.size(); i++) {
                    if (ApiMockHelper.paramSet.contains(formBody.name(i))) {
                        pathParams.append("--")
                                .append(formBody.name(i))
                                .append("--")
                                .append(formBody.value(i));
                    }
                }
            }
        }

        //获取request的创建者builder
        HttpUrl oldHttpUrl = request.url();
        HttpUrl newHttpUrl = oldHttpUrl
                .newBuilder()
                .scheme("http")
                .host(ApiMockHelper.host)
                .port(5000)
                .encodedPath("/" + oldHttpUrl.encodedPath().replace("/", "__").substring(2) + pathParams.toString())
                .build();
        Request.Builder builder = request.newBuilder();
        return chain.proceed(builder.url(newHttpUrl).build());
    }
}
