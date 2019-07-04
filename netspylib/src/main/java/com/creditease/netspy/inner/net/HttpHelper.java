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

    public static void uploadFile(String url, File file) {
        OkHttpClient client = new OkHttpClient();

        FormBody paramsBody = new FormBody.Builder()
            .add("name", "")
            .build();

        MediaType type = MediaType.parse("application/octet-stream");
        RequestBody fileBody = RequestBody.create(type, file);

        RequestBody multipartBody = new MultipartBody.Builder()
            .setType(MultipartBody.ALTERNATIVE)
            //一样的效果
            .addPart(Headers.of(
                "Content-Disposition",
                "form-data; name=\"params\"")
                , paramsBody)
            .addPart(Headers.of(
                "Content-Disposition",
                "form-data; name=\"file\"; filename=\"cnetspy.db\"")
                , fileBody)
            .build();

        Request request = new Request.Builder().url(url)
            .addHeader("User-Agent", "android")
            .header("Content-Type", "text/html; charset=utf-8;")
            .post(multipartBody)
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
