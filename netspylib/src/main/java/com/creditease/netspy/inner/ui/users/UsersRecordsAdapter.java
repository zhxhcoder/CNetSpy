package com.creditease.netspy.inner.ui.users;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.UsersItemData;
import com.creditease.netspy.inner.support.FormatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxh on 2020/6/10
 */

public class UsersRecordsAdapter extends RecyclerView.Adapter<UsersRecordsAdapter.ViewHolder> {
    private List<UsersItemData> dataList = new ArrayList<>();
    private Activity activity;
    private Handler handler;

    public UsersRecordsAdapter(Activity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_records_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final UsersItemData data = dataList.get(i);
        holder.name.setText(data.getName());

        if (!TextUtils.isEmpty(data.timestamp)) {
            holder.time.setText(FormatHelper.timeStamp2Str(Long.parseLong(data.timestamp), null));
        } else {
            holder.time.setText("yyyy-MM-dd HH:mm:ss");
        }


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void setData(List<UsersItemData> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView show;
        public final TextView time;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            show = view.findViewById(R.id.show);
            time = view.findViewById(R.id.time);
        }
    }
}