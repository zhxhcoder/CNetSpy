package com.creditease.netspy.inner.support;

import android.util.Log;

import com.creditease.netspy.ApiMockHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhxh on 2020/6/8
 */
public class OkHttpHelper {
    private static final String TAG = "OkHttpHelper";
    private static OkHttpHelper instance;

    private OkHttpHelper() {
    }

    public static OkHttpHelper getInstance() {
        if (instance == null) {
            instance = new OkHttpHelper();
        }
        return instance;
    }

    public interface HttpCallBack {
        void onSuccess(String resp);
    }

    public void getRecords(HttpCallBack cb) {
        String url = "http://" + ApiMockHelper.host + ":5000/api/records";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() == null) {
                    return;
                }
                String resp = response.body().string();
                Log.d(TAG, "onResponse: " + resp);

                if (cb != null) {
                    cb.onSuccess(resp);
                }
            }
        });
    }

    public void postRecords(String path, String resp_data, int show_type, HttpCallBack cb) {
        String url = "http://" + ApiMockHelper.host + ":5000/api/records";

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("path", path)
                .add("resp_data", resp_data)
                .add("show_type", String.valueOf(show_type))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());

                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }

                if (response.body() == null) {
                    return;
                }
                String resp = response.body().string();
                Log.d(TAG, "onResponse: " + resp);

                if (cb != null) {
                    cb.onSuccess(resp);
                }
            }
        });
    }
}