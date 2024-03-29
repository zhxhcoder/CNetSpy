package com.creditease.netspy.demo.netspy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.creditease.netspy.ApiMockHelper;
import com.creditease.netspy.ApiMockInterceptor;
import com.creditease.netspy.BugSpyHelper;
import com.creditease.netspy.NetSpyHelper;
import com.creditease.netspy.NetSpyInterceptor;
import com.creditease.netspy.demo.R;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
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
public class NetActivity extends AppCompatActivity {
    private final static String TAG = "sampleSpyLog";
    TextView tvHttpContent;
    Button btn_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_spy);
        checkOnlineState();

        tvHttpContent = findViewById(R.id.tvHttpContent);
        btn_test = findViewById(R.id.btn_test);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 测试

                try {
                    URI uri = new URI("http://10.24.119.254:5000/mock/records.html?source=%E6%8C%87%E6%97%BA");
                    btn_test.setText(uri.getHost());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });

        findViewById(R.id.btn_http_post).setOnClickListener(view -> {
            for (int i = 0; i < 1; i++) {
                doHttpPost();
            }
        });
        findViewById(R.id.btn_http_get).setOnClickListener(view -> {
            for (int i = 0; i < 1; i++) {
                doHttpGet();
            }
        });

        findViewById(R.id.btn_mock).setOnClickListener(v -> startActivity(new Intent(NetActivity.this, SampleMockApiActivity.class)));

        CheckBox checkBox1 = findViewById(R.id.cb_netspy_status);
        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(NetActivity.this, "是否开启 " + isChecked, Toast.LENGTH_LONG).show();
            NetSpyHelper.debug(isChecked);
            NetSpyHelper.launchActivity(this);
        });
        CheckBox checkBox2 = findViewById(R.id.cb_bugspy_status);
        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(NetActivity.this, "是否开启 " + isChecked, Toast.LENGTH_LONG).show();
            BugSpyHelper.debug(isChecked);
            BugSpyHelper.launchActivity(this);
        });
        CheckBox checkBox3 = findViewById(R.id.cb_apimock_status);
        checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(NetActivity.this, "是否开启 " + isChecked, Toast.LENGTH_LONG).show();
            ApiMockHelper.debug(isChecked);
            ApiMockHelper.launchActivity(this);
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
                .addInterceptor(new ApiMockInterceptor())
                .addInterceptor(new NetSpyInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private void doHttpPost() {
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

        Map<String, String> map = new HashMap<>();
        map.put("method", "m");
        map.put("nice", "n");
        map.put("fundType", "1");
        map.put("cardids", "c");
        api.postList(map).enqueue(cb);
    }

    private void doHttpGet() {
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

        Map<String, String> map = new HashMap<>();
        map.put("method", "m");
        map.put("nice", "n");
        map.put("fundType", "1");
        map.put("cardids", "c");
        api.getList(map).enqueue(cb);
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
