package com.creditease.netspy.inner.ui.apimock;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.support.FormatHelper;
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
        OkHttpHelper.getInstance().getRecords(resp -> {
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

    private static class ApiMockListAdapter extends RecyclerView.Adapter<ApiMockListAdapter.ViewHolder> {
        private List<ApiMockData> dataList;
        private String filterText;
        private Activity activity;

        public ApiMockListAdapter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.netspy_list_api_item, parent, false);
            ViewHolder holder = new ViewHolder(itemView);
            itemView.setTag(holder);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
            final ApiMockData data = dataList.get(i);
            String pathStr = data.getMockPath();
            holder.path.setText(FormatHelper.findSearch(Color.BLUE, pathStr, filterText));
            holder.show.setText(data.getShowType());

            holder.view.setOnClickListener(v -> ApiMockDetailActivity.start(activity, data));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        void setData(String filterText, List<ApiMockData> dataList) {
            this.filterText = filterText;
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final TextView path;
            public final TextView show;

            ViewHolder(View view) {
                super(view);
                this.view = view;
                path = view.findViewById(R.id.path);
                show = view.findViewById(R.id.show);
            }
        }
    }
}