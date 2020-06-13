package com.creditease.netspy.inner.ui.apimock;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.support.FormatHelper;
import com.creditease.netspy.inner.support.OkHttpHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhxh on 2020/6/10
 */

public class ApiMockListAdapter extends RecyclerView.Adapter<ApiMockListAdapter.ViewHolder> {
    private List<ApiMockData> dataList = new ArrayList<>();
    private String filterText;
    private Activity activity;
    private Handler handler;

    public ApiMockListAdapter(Activity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
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

        if ("-1".equals(data.getShowType())) {
            holder.show.setBackgroundColor(ContextCompat.getColor(activity, R.color.netspy_status_500));
        } else if ("0".equals(data.getShowType())) {
            holder.show.setBackgroundColor(ContextCompat.getColor(activity, R.color.netspy_status_400));
        } else {
            holder.show.setBackgroundColor(ContextCompat.getColor(activity, R.color.netspy_status_default));
        }
        holder.time.setText(FormatHelper.timeStamp2Str(Long.parseLong(data.timestamp), null));

        holder.view.setOnClickListener(v -> ApiMockDetailActivity.start(activity, data));
        holder.delete.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(activity))
                    .setTitle("FBI警告")
                    .setMessage("此操作将同时删除远程服务器上的数据，删除后其他人将无法请求改接口，如有不满意之处，这边建议您点击进入修改页面修改")
                    .setPositiveButton("放弃", null)
                    .setNegativeButton("任性", (dialog1, which) -> OkHttpHelper.getInstance().deleteApiItem(data.path, handler))
                    .create();
            dialog.show();
        });
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
        public final TextView time;
        public final ImageView delete;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            path = view.findViewById(R.id.path);
            show = view.findViewById(R.id.show);
            time = view.findViewById(R.id.time);
            delete = view.findViewById(R.id.delete);
        }
    }
}