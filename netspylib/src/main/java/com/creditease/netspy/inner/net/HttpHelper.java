package com.creditease.netspy.inner.net;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by zhxh on 2019/07/03
 * 上传数据库
 */
public class HttpHelper {

    public static void postFile(String url, String fileName) {

        OkHttpClient okHttpClient = new OkHttpClient();

        File file = new File(fileName);

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file))
            .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("XXXXXXXX", "onResponse: " + response.body().string());
            }
        });
    }


    public static void uploadFile(String url, File file) {
        OkHttpClient client = new OkHttpClient();

        FormBody paramsBody = new FormBody.Builder()
            .add("id", "")
            .add("name", "")
            .build();

        //二种：文件请求体
        MediaType type = MediaType.parse("application/octet-stream");//"text/xml;charset=utf-8"
        RequestBody fileBody = RequestBody.create(type, file);


        //三种：混合参数和文件请求
        RequestBody multipartBody = new MultipartBody.Builder()
            .setType(MultipartBody.ALTERNATIVE)
            //一样的效果
            .addPart(Headers.of(
                "Content-Disposition",
                "form-data; name=\"params\"")
                , paramsBody)
            .addPart(Headers.of(
                "Content-Disposition",
                "form-data; name=\"file\"; filename=\"plans.xml\"")
                , fileBody)
            .build();

        Request request = new Request.Builder().url(url)
            .addHeader("User-Agent", "android")
            .header("Content-Type", "text/html; charset=utf-8;")
            .post(multipartBody)//传参数、文件或者混合，改一下就行请求体就行
            .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("XXXXXXXX", "onResponse: " + response.body().string());
                }
            }
        });

    }


    interface CallBack {

    }
}
