package com.creditease.netspy.inner.ui.apimock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.support.OkHttpHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxh on 2020/06/06
 */
public class ApiMockListActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener {

    List<ApiMockData> dataList = new ArrayList<>();
    private String filterText = "";

    RecyclerView recyclerView;

    ApiMockListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netspy_fragment_list);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        adapter = new ApiMockListAdapter(this);
        recyclerView.setAdapter(adapter);

        downLoadApi();
    }

    private void downLoadApi() {
        OkHttpHelper.getInstance().getApiRecords(resp -> {
            try {
                dataList = new Gson().fromJson(resp, new TypeToken<List<ApiMockData>>() {
                }.getType());

            } catch (JsonSyntaxException e) {
                dataList = new ArrayList<>();
                e.printStackTrace();
            }
            adapter.setData(filterText, dataList);
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filterText = s;
        adapter.setData(filterText, dataList);
        return true;
    }

}