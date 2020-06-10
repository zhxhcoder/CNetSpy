package com.creditease.netspy.inner.ui.apimock;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.ui.netspy.HttpTabActivity;

/**
 * Created by zhxh on 2020/06/06
 */
public class ApiMockDetailActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String ARG_API_MOCK_DATA = "api_mock_data";

    private ApiMockData data;
    private String filterText = "";

    TextView path;
    TextView resp_data;
    TextView resp_empty;
    TextView resp_error;

    private RadioButton radio1;
    private RadioButton radio2;
    private RadioButton radio3;


    public static void start(Context context, ApiMockData data) {
        Intent intent = new Intent(context, HttpTabActivity.class);
        intent.putExtra(ARG_API_MOCK_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_mock_detail);

        path = findViewById(R.id.path);
        resp_data = findViewById(R.id.resp_data);
        resp_empty = findViewById(R.id.resp_empty);
        resp_error = findViewById(R.id.resp_error);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);


        data = (ApiMockData) getIntent().getSerializableExtra(ARG_API_MOCK_DATA);


    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filterText = s;

        return true;
    }
}