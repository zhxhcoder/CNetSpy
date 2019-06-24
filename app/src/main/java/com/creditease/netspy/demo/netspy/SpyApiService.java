package com.creditease.netspy.demo.netspy;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

    static class Data {
        final String params;

        Data(String params) {
            this.params = params;
        }
    }

    interface HttpApi {
        @GET("zhxh/list")
        Call<Void> getList();

        @GET("zhxh/array")
        Call<Void> getArray();

        @POST("zhxh/list")
        Call<Void> postList(@Body Data body);

        @POST("zhxh/array")
        Call<Void> postArray(@Body Data body);
    }
}