package com.creditease.netspy.demo.netspy;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
                .baseUrl("http://10.106.157.94:5000")
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

        /////////////////////////////////////////////////////////////////////////

        @FormUrlEncoded
        @POST("/api/records")
        Call<Void> postMockRecords(@FieldMap Map<String, String> map);

        @GET("/api/records")
        Call<Void> getsMockRecords();

        @GET("/mock__api.action")
        Call<Void> getMockItem();

        /////////////////////////////////////////////////////////////////////////

        @GET("zhxh/list")
        Call<Void> getList();

        @POST("zhxh/list")
        Call<Void> postList(@Body Data body);

    }
}