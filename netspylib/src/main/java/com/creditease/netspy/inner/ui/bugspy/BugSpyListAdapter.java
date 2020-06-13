package com.creditease.netspy.inner.ui.bugspy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.BugEvent;
import com.creditease.netspy.inner.support.FormatHelper;

import java.util.List;

/**
 * Created by zhxh on 2019/07/16
 */
class BugSpyListAdapter extends RecyclerView.Adapter<BugSpyListAdapter.ViewHolder> {
    Context context;
    IBugTabFragment fragment;
    private List<BugEvent> dataList;
    boolean isRemote;

    BugSpyListAdapter(boolean isRemote, IBugTabFragment fragment, Context context) {
        this.isRemote = isRemote;
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bugspy_list_bug_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BugEvent item = dataList.get(position);

        holder.index.setText(String.valueOf(position + 1));

        String strTime = FormatHelper.getHHmmSS(item.getCrashDate());
        holder.time.setText(strTime);

        holder.bug.setText(item.getBugSummary());

        holder.delete.setOnClickListener(v -> {
            fragment.updateBugData(item);
            Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
        });

        holder.email.setOnClickListener(v -> {
            sendEmail(item.getBugReport());
        });
        holder.view.setOnClickListener(v -> {
            BugSpyDetailActivity.start(context, item.getTimeStamp());
        });
    }

    void setData(List<BugEvent> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView index;
        public final TextView time;
        public final ImageView email;
        public final ImageView delete;
        public final TextView bug;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            index = view.findViewById(R.id.index);
            time = view.findViewById(R.id.time);
            email = view.findViewById(R.id.email);
            delete = view.findViewById(R.id.delete);
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
        context.startActivity(Intent.createChooser(sendIntent, "Title:"));
    }
}
