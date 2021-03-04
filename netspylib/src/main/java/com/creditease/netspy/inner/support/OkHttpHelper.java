package com.creditease.netspy.inner.support;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

    public static final int SAVE_SUCCESS = 1001;
    public static final int LIST_SUCCESS = 1002;
    public static final int DEL_SUCCESS = 1003;

    private OkHttpHelper() {
    }

    public static OkHttpHelper getInstance() {
        if (instance == null) {
            instance = new OkHttpHelper();
        }
        return instance;
    }

    public void deleteApiItem(String url, Handler handler) {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .delete()//默认就是GET请求，可以不写
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
                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = DEL_SUCCESS;
                    msg.obj = resp;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void getApiRecords(Handler handler) {
        String url = ApiMockHelper.getBaseURL() + "api/records";
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
                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = LIST_SUCCESS;
                    msg.obj = resp;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void postApiRecords(String path, String resp_data) {
        postApiRecords(path, 1, resp_data, "", "", null);
    }


    public void postApiRecords(String path, int show_type, String resp_data, String resp_empty, String resp_error, Handler handler) {
        String url = ApiMockHelper.getBaseURL() + "api/records";

        String trimPath;
        if (path.startsWith("/")) {
            trimPath = path.substring(1).trim();
        } else {
            trimPath = path.trim();
        }

        if (TextUtils.isEmpty(trimPath)) {
            return;
        }

        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("path", trimPath);
        builder.add("show_type", String.valueOf(show_type));

        if (!TextUtils.isEmpty(resp_data)) {
            builder.add("resp_data", resp_data.trim());
        }
        if (!TextUtils.isEmpty(resp_empty)) {
            builder.add("resp_empty", resp_empty.trim());
        }
        if (!TextUtils.isEmpty(resp_error)) {
            builder.add("resp_error", resp_error.trim());
        }
        RequestBody requestBody = builder.build();

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

                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = SAVE_SUCCESS;
                    msg.obj = resp;
                    handler.sendMessage(msg);
                }
            }
        });
    }


    /***************************************Track记录*********************************************/

    public void postTrackRecords(String source, String user, String event, String report, Handler handler) {
        String url = ApiMockHelper.getBaseURL() + "track/records";

        if (TextUtils.isEmpty(event)) {
            return;
        }
        if (TextUtils.isEmpty(report)) {
            return;
        }

        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("event", event.trim());
        builder.add("report", report.trim());

        if (!TextUtils.isEmpty(source)) {
            builder.add("app", source.trim());
        }
        if (!TextUtils.isEmpty(user)) {
            builder.add("user", user.trim());
        }

        RequestBody requestBody = builder.build();

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

                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = SAVE_SUCCESS;
                    msg.obj = resp;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /***************************************BUG记录*********************************************/


    public void deleteBugItem(String timestamp, Handler handler) {
        String url = ApiMockHelper.getBaseURL() + "bug/item/" + timestamp;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .delete()//默认就是GET请求，可以不写
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
                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = DEL_SUCCESS;
                    msg.obj = resp;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void getBugRecords(Handler handler) {
        String url = ApiMockHelper.getBaseURL() + "bug/records";
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
                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = LIST_SUCCESS;
                    msg.obj = resp;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void postBugRecords(String timestamp, String summary, String report, String device, String user, String app, Handler handler) {
        String url = ApiMockHelper.getBaseURL() + "bug/records";

        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("timestamp", timestamp);
        builder.add("summary", summary);
        builder.add("report", report);
        builder.add("device", device);
        builder.add("user", user);
        builder.add("app", app);

        RequestBody requestBody = builder.build();

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

                if (handler != null) {
                    Message msg = handler.obtainMessage();
                    msg.what = SAVE_SUCCESS;
                    msg.obj = resp;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
