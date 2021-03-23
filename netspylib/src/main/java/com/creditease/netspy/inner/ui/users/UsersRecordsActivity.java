package com.creditease.netspy.inner.ui.users;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.UsersItemData;
import com.creditease.netspy.inner.support.OkHttpHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class UsersRecordsActivity extends AppCompatActivity {
    List<UsersItemData> dataList = new ArrayList<>();
    RecyclerView recyclerView;
    UsersRecordsAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_records_layout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("用户记录");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        adapter = new UsersRecordsAdapter(this, handler);
        recyclerView.setAdapter(adapter);
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == OkHttpHelper.LIST_SUCCESS) {
                try {
                    dataList = new Gson().fromJson(String.valueOf(msg.obj), new TypeToken<List<UsersItemData>>() {
                    }.getType());
                    if (dataList == null || dataList.isEmpty()) {
                        return;
                    }
                    toolbar.setTitle("用户-" + dataList.size() + "条");
                    adapter.setData(dataList);
                } catch (JsonSyntaxException e) {
                    dataList = new ArrayList<>();
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        }
    };
}