package com.creditease.netspy.inner.ui.bugspy;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxh on 2019/07/16
 */
class BugSpyListAdapter extends RecyclerView.Adapter<BugSpyListAdapter.ViewHolder> {
    Context context;
    IBugTabFragment fragment;
    private List<BugEvent> dataList=new ArrayList<>();
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

        holder.bug.setText(item.getSummary());

        holder.delete.setOnClickListener(v -> {
            fragment.updateBugData(item);
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
        public final ImageView delete;
        public final TextView bug;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            index = view.findViewById(R.id.index);
            time = view.findViewById(R.id.time);
            delete = view.findViewById(R.id.delete);
            bug = view.findViewById(R.id.bug);
        }
    }
}
