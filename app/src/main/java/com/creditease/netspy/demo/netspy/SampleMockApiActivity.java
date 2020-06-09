package com.creditease.netspy.demo.netspy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.creditease.netspy.NetSpyInterceptor;
import com.creditease.netspy.demo.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleMockApiActivity extends AppCompatActivity {
    EditText et_path, et_resp, et_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_mock_api);

        et_path = findViewById(R.id.et_path);
        et_resp = findViewById(R.id.et_resp);
        et_show = findViewById(R.id.et_show);

        findViewById(R.id.btn_api_records_post).setOnClickListener(view -> {
            doHttpAPIList(0);
        });
        findViewById(R.id.btn_api_records_get).setOnClickListener(view -> {
            doHttpAPIList(1);
        });
        findViewById(R.id.btn_api_get_item).setOnClickListener(view -> {
            doHttpAPIList(2);
        });
    }


    private void doHttpAPIList(int type) {
        SpyApiService.HttpApi api = SpyApiService.getMock(getClient(this));
        Callback<Void> cb = new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() == null) {
                    return;
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        };

        if (type == 0) {
            Map<String, String> postMap = new HashMap<>();
            postMap.put("path", et_path.getText().toString());
            postMap.put("resp_data", et_resp.getText().toString());
            postMap.put("show_type", et_show.getText().toString());
            api.postMockRecords(postMap).enqueue(cb);
        } else if (type == 1) {
            api.getsMockRecords().enqueue(cb);
        } else {
            api.getMockItem(et_path.getText().toString()).enqueue(cb);
        }
    }

    private OkHttpClient getClient(Context context) {
        return new OkHttpClient.Builder()
                // Add a NetSpyInterceptor instance to your OkHttp client
                .addInterceptor(new NetSpyInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }
}