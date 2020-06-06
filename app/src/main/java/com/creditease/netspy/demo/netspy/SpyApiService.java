package com.creditease.netspy.demo.netspy;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by zhxh on 2019/06/24
 */
class SpyApiService {

    static HttpApi getInstance(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.easy-mock.com/mock/5c10abcd8c59f04d2e3a7722/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(HttpApi.class);
    }


    static HttpApi getMock(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.106.156.200:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(HttpApi.class);
    }


    static class Data {
        final String params;

        Data(String params) {
            this.params = params;
        }
    }

    interface HttpApi {
        @GET("/todos")
        Call<Void> getMockTodos(@QueryMap Map<String, String> map);

        @FormUrlEncoded
        @POST("/todos")
        Call<Void> postMockTodos(@FieldMap Map<String, String> map);

        @GET("zhxh/list")
        Call<Void> getList();

        @POST("zhxh/list")
        Call<Void> postList(@Body Data body);

    }
}