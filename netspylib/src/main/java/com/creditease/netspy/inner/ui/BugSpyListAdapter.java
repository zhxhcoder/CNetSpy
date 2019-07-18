package com.creditease.netspy.inner.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.BugEvent;

import java.util.List;

/**
 * Created by zhxh on 2019/07/16
 */
class BugSpyListAdapter extends RecyclerView.Adapter<BugSpyListAdapter.ViewHolder> {
    Context activity;
    private List<BugEvent> dataList;

    BugSpyListAdapter(Context context) {
        activity = context;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.netspy_list_bug_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BugEvent item = dataList.get(position);

        holder.time.setText(item.getCrashDate().toLocaleString());
        holder.bug.setText(item.getBugReport());

        holder.email.setOnClickListener(v -> {
            sendEmail(item.getBugReport());
            //TODO
        });
        holder.view.setOnClickListener(v -> {

            //TODO
        });
    }


    void setData(List<BugEvent> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView time;
        public final ImageView email;
        public final TextView bug;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            time = view.findViewById(R.id.time);
            email = view.findViewById(R.id.email);
            bug = view.findViewById(R.id.bug);

        }
    }


    public void sendEmail(String trace) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String subject = "App异常报告";
        String body = "异常日志记录如下: " + "\n" + trace + "\n";

        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"zhxhcoder@gmail.com"});
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.setType("message/rfc822");
        activity.startActivity(Intent.createChooser(sendIntent, "Title:"));
    }
}
