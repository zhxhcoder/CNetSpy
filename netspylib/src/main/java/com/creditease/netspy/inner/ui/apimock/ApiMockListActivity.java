package com.creditease.netspy.inner.ui.apimock;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
        setContentView(R.layout.netspy_api_mock_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        adapter = new ApiMockListAdapter(this, handler);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        downLoadApi();
    }

    private void downLoadApi() {
        OkHttpHelper.getInstance().getApiRecords(handler);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == OkHttpHelper.LIST_SUCCESS) {
                try {
                    dataList = new Gson().fromJson(String.valueOf(msg.obj), new TypeToken<List<ApiMockData>>() {
                    }.getType());
                    if (dataList == null || dataList.isEmpty()) {
                        return;
                    }
                    adapter.setData(filterText, dataList);
                } catch (JsonSyntaxException e) {
                    dataList = new ArrayList<>();
                    e.printStackTrace();
                }
            } else if (msg.what == OkHttpHelper.ITEM_SUCCESS) {
                downLoadApi();
                Toast.makeText(ApiMockListActivity.this, "删除成功,重新请求数据", Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(OkHttpHelper.LIST_SUCCESS);
        handler.removeMessages(OkHttpHelper.ITEM_SUCCESS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.netspy_api, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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