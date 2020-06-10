package com.creditease.netspy.inner.ui.apimock;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.support.FormatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxh on 2020/6/10
 */

public class ApiMockListAdapter extends RecyclerView.Adapter<ApiMockListAdapter.ViewHolder> {
    private List<ApiMockData> dataList=new ArrayList<>();
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