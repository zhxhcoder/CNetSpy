package com.creditease.netspy.demo.netspy;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by zhxh on 2019/06/24
 */
class SpyApiService {

    static HttpApi getInstance(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.weather.com.cn")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(HttpApi.class);
    }


    static HttpApi getMock(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.24.119.254:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(HttpApi.class);
    }

    interface HttpApi {

        /////////////////////////////////////////////////////////////////////////

        @FormUrlEncoded
        @POST("/api/records")
        Call<String> postMockRecords(@FieldMap Map<String, String> map);

        @GET("/api/records")
        Call<String> getsMockRecords();

        @GET("/{path}")
        Call<String> getMockItem(@Path("path") String path);

        /////////////////////////////////////////////////////////////////////////

        @GET("/data/sk/101010100.html")
        Call<Void> getList(@QueryMap Map<String, String> map);

        @FormUrlEncoded
        @POST("/data/sk/101010100.html")
        Call<Void> postList(@FieldMap Map<String, String> map);

    }
}