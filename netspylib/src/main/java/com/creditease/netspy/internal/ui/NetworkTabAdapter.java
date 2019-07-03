package com.creditease.netspy.internal.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.internal.db.HttpEvent;
import com.creditease.netspy.internal.support.FormatUtils;
import com.creditease.netspy.internal.ui.NetSpyListFragment.OnListFragmentInteractionListener;

import java.text.MessageFormat;
import java.util.List;

class NetworkTabAdapter extends RecyclerView.Adapter<NetworkTabAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener listener;

    private final int colorDefault;
    private final int colorRequested;
    private final int colorError;
    private final int color500;
    private final int color400;
    private final int color300;

    private List<HttpEvent> dataList;

    NetworkTabAdapter(Context context, OnListFragmentInteractionListener listener) {
        this.listener = listener;
        colorDefault = ContextCompat.getColor(context, R.color.netspy_status_default);
        colorRequested = ContextCompat.getColor(context, R.color.netspy_status_requested);
        colorError = ContextCompat.getColor(context, R.color.netspy_status_error);
        color500 = ContextCompat.getColor(context, R.color.netspy_status_500);
        color400 = ContextCompat.getColor(context, R.color.netspy_status_400);
        color300 = ContextCompat.getColor(context, R.color.netspy_status_300);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.netspy_list_network_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HttpEvent transaction = dataList.get(position);

        holder.path.setText(MessageFormat.format("{0} {1}", transaction.getMethod(), transaction.getPath()));
        holder.host.setText(transaction.getHost());
        holder.start.setText(FormatUtils.getHHmmSS(transaction.getRequestDate()));
        holder.ssl.setVisibility(transaction.isSsl() ? View.VISIBLE : View.GONE);
        if (transaction.getStatus() == HttpEvent.Status.Complete) {
            holder.code.setText(String.valueOf(transaction.getResponseCode()));
            holder.duration.setText(transaction.getDurationString());
            holder.size.setText(transaction.getTotalSizeString());
        } else {
            holder.code.setText(null);
            holder.duration.setText(null);
            holder.size.setText(null);
        }
        if (transaction.getStatus() == HttpEvent.Status.Failed) {
            holder.code.setText("!!!");
        }
        setStatusColor(holder, transaction);
        holder.transaction = transaction;
        holder.view.setOnClickListener(v -> {
            if (null != NetworkTabAdapter.this.listener) {
                NetworkTabAdapter.this.listener.onListFragmentInteraction(holder.transaction);
            }
        });
    }

    private void setStatusColor(ViewHolder holder, HttpEvent transaction) {
        int color;
        if (transaction.getStatus() == HttpEvent.Status.Failed) {
            color = colorError;
        } else if (transaction.getStatus() == HttpEvent.Status.Requested) {
            color = colorRequested;
        } else if (transaction.getResponseCode() >= 500) {
            color = color500;
        } else if (transaction.getResponseCode() >= 400) {
            color = color400;
        } else if (transaction.getResponseCode() >= 300) {
            color = color300;
        } else {
            color = colorDefault;
        }
        holder.code.setTextColor(color);
        holder.path.setTextColor(color);
    }

    void setData(List<HttpEvent> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView code;
        public final TextView path;
        public final TextView host;
        public final TextView start;
        public final TextView duration;
        public final TextView size;
        public final TextView ssl;
        HttpEvent transaction;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            code = view.findViewById(R.id.code);
            path = view.findViewById(R.id.path);
            host = view.findViewById(R.id.host);
            start = view.findViewById(R.id.start);
            duration = view.findViewById(R.id.duration);
            size = view.findViewById(R.id.size);
            ssl = view.findViewById(R.id.ssl);
        }
    }
}
