package com.creditease.netspy.inner.ui.apimock;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.ui.netspy.HttpTabActivity;

/**
 * Created by zhxh on 2020/06/06
 */
public class ApiMockDetailActivity extends AppCompatActivity {
    private static final String ARG_API_MOCK_DATA = "api_mock_data";

    public static void start(Context context, ApiMockData data) {
        Intent intent = new Intent(context, HttpTabActivity.class);
        intent.putExtra(ARG_API_MOCK_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_mock_detail);
    }
}