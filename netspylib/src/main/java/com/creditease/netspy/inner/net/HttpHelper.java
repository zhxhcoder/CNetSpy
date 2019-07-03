package com.creditease.netspy.inner.net;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
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


    interface CallBack {

    }
}
