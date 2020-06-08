package com.creditease.netspy.demo.netspy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import com.creditease.netspy.BugSpyHelper;
import com.creditease.netspy.NetSpyHelper;
import com.creditease.netspy.NetSpyInterceptor;
import com.creditease.netspy.demo.R;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;
import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;

/**
 * Created by zhxh on 2019/06/24
 */
public class SampleSpyActivity extends AppCompatActivity {
    private final static String TAG = "sampleSpyLog";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_spy);

        checkOnlineState();

        findViewById(R.id.btn_http).setOnClickListener(view -> {
            for (int i = 0; i < 2; i++) {
                forceSendRequestByMobileData(this);
            }
        });
        findViewById(R.id.btn_api_list).setOnClickListener(view -> {
            doHttpAPIList(0);

        });
        findViewById(R.id.btn_todo_number).setOnClickListener(view -> {
            doHttpAPIList(1);

        });

        CheckBox checkBox1 = findViewById(R.id.cb_netspy_status);
        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(SampleSpyActivity.this, "是否开启 " + isChecked, Toast.LENGTH_LONG).show();
            NetSpyHelper.debug(isChecked);
            NetSpyHelper.launchActivity(this);
        });
        CheckBox checkBox2 = findViewById(R.id.cb_bugspy_status);
        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(SampleSpyActivity.this, "是否开启 " + isChecked, Toast.LENGTH_LONG).show();
            BugSpyHelper.debug(isChecked);
            BugSpyHelper.launchActivity(this);
        });

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1001) {
                boolean isReachable = (boolean) msg.obj;
                if (isReachable) {
                    Log.d(TAG, "host 可达 ");
                } else {
                    Log.d(TAG, "host 不可达 ");
                }
            }
        }
    };

    private OkHttpClient getClient(Context context) {
        return new OkHttpClient.Builder()
                // Add a NetSpyInterceptor instance to your OkHttp client
                .addInterceptor(new NetSpyInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private void doHttpActivity() {
        SpyApiService.HttpApi api = SpyApiService.getInstance(getClient(this));
        Callback<Void> cb = new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        };
        api.getList().enqueue(cb);
        api.getList().enqueue(cb);
        api.postList(new SpyApiService.Data("list")).enqueue(cb);
    }

    private void doHttpAPIList(int type) {
        SpyApiService.HttpApi api = SpyApiService.getMock(getClient(this));
        Callback<Void> cb = new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        };

        if (type == 0) {
            Map<String, String> postMap = new HashMap<>();
            postMap.put("path", "todo__no5.action");
            postMap.put("resp", "55555");
            api.postMockTodos(postMap).enqueue(cb);
        } else {
            Map<String, String> getMap = new HashMap<>();
            api.getMockTodos(getMap).enqueue(cb);
        }
    }


    @TargetApi(21)
    private void forceSendRequestByMobileData(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NET_CAPABILITY_INTERNET);
        //强制使用蜂窝数据网络-移动数据
        builder.addTransportType(TRANSPORT_CELLULAR);
        NetworkRequest build = builder.build();
        connectivityManager.requestNetwork(build, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                try {
                    doHttpActivity();

                } catch (Exception e) {

                }
            }
        });
    }


    public void checkOnlineState() {
        ConnectivityManager CManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NInfo = CManager.getActiveNetworkInfo();
        if (NInfo != null && NInfo.isConnectedOrConnecting()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        boolean isReachable = InetAddress.getByName("10.106.156.200").isReachable(10000);

                        Message message = handler.obtainMessage();
                        message.what = 1001;
                        message.obj = isReachable;
                        handler.sendMessage(message);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
