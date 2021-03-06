package com.creditease.netspy.inner.ui.netspy;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.support.FormatHelper;
import com.creditease.netspy.inner.ui.netspy.NetSpyListFragment.OnListFragmentInteractionListener;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by zhxh on 2019/07/18
 * 网络数据列表.
 */
class NetSpyListAdapter extends RecyclerView.Adapter<NetSpyListAdapter.ViewHolder> {

    NetSpyListFragment fragment;
    Context context;

    private final OnListFragmentInteractionListener listener;

    private final int colorDefault;
    private final int colorRequested;
    private final int colorError;
    private final int color500;
    private final int color400;
    private final int color300;

    private List<HttpEvent> dataList;
    private String filterText;

    NetSpyListAdapter(NetSpyListFragment fragment, Context context, OnListFragmentInteractionListener listener) {
        this.fragment = fragment;
        this.context = context;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.netspy_list_http_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HttpEvent httpEvent = dataList.get(position);

        String pathStr = MessageFormat.format("{0} {1}", httpEvent.getMethod(), httpEvent.getMockPath());
        holder.path.setText(FormatHelper.findSearch(Color.BLUE, pathStr, filterText));
        holder.host.setText(httpEvent.getHost());
        String strTime = FormatHelper.getHHmmSS(httpEvent.getRequestDate());
        if (strTime.length() > 5) {
            holder.start.setText(strTime.substring(5));
        } else {
            holder.start.setText(strTime);
        }

        if (httpEvent.getStatus() == HttpEvent.Status.Complete) {
            holder.code.setText(String.valueOf(httpEvent.getResponseCode()));
        } else {
            holder.code.setText(null);
        }
        if (httpEvent.getStatus() == HttpEvent.Status.Failed) {
            holder.code.setText("!!!");
        }
        setRequestText(holder.param, httpEvent.getFormattedRequestBody(), httpEvent.getRequestBodyIsPlainText());

        setStatusColor(holder, httpEvent);

        if (httpEvent.isUploaded()) {
            holder.upload.setImageResource(R.drawable.netspy_ic_cloud_upload_yellow_24);
        } else {
            holder.upload.setImageResource(R.drawable.netspy_ic_cloud_upload_black_24);
        }
        holder.upload.setVisibility(View.VISIBLE);

        holder.transaction = httpEvent;
        holder.view.setOnClickListener(v -> {
            if (null != NetSpyListAdapter.this.listener) {
                NetSpyListAdapter.this.listener.onListFragmentInteraction(holder.transaction);
            }
        });
        holder.delete.setOnClickListener(v -> {
            fragment.updateDataFromDelete(httpEvent);
        });
        holder.upload.setOnClickListener(v -> {
            fragment.uploadCloudFromDb(httpEvent);
        });
    }

    private void setRequestText(TextView requestBody, String bodyString, boolean isPlainText) {

        if (TextUtils.isEmpty(bodyString)) {
            requestBody.setVisibility(View.GONE);
        } else {
            requestBody.setVisibility(View.VISIBLE);

            if (!isPlainText) {
                requestBody.setText(context.getString(R.string.netspy_body_omitted));
            } else {
                requestBody.setText(bodyString);
            }
        }

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

    void setData(String filterText, List<HttpEvent> dataList) {
        this.filterText = filterText;
        this.dataList = dataList;
        notifyDataSetChanged();
        listener.onRefreshTitle(dataList.size() + "条记录");
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView code;
        public final TextView path;
        public final TextView host;
        public final TextView start;
        public final TextView param;
        public final ImageView delete;
        public final ImageView upload;
        HttpEvent transaction;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            code = view.findViewById(R.id.code);
            path = view.findViewById(R.id.path);
            host = view.findViewById(R.id.host);
            start = view.findViewById(R.id.start);
            param = view.findViewById(R.id.param);
            delete = view.findViewById(R.id.delete);
            upload = view.findViewById(R.id.upload);
        }
    }
}
