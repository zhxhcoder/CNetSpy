package com.creditease.netspy.inner.ui.netspy;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.data.HttpTransaction;
import com.creditease.netspy.inner.data.LocalCupboard;
import com.creditease.netspy.inner.ui.netspy.NetworkListFragment.OnListFragmentInteractionListener;

class NetworkTabAdapter extends RecyclerView.Adapter<NetworkTabAdapter.ViewHolder> {

    private final Context context;
    private final OnListFragmentInteractionListener listener;
    private final CursorAdapter cursorAdapter;

    private final int colorDefault;
    private final int colorRequested;
    private final int colorError;
    private final int color500;
    private final int color400;
    private final int color300;

    NetworkTabAdapter(Context context, OnListFragmentInteractionListener listener) {
        this.listener = listener;
        this.context = context;
        colorDefault = ContextCompat.getColor(context, R.color.netspy_status_default);
        colorRequested = ContextCompat.getColor(context, R.color.netspy_status_requested);
        colorError = ContextCompat.getColor(context, R.color.netspy_status_error);
        color500 = ContextCompat.getColor(context, R.color.netspy_status_500);
        color400 = ContextCompat.getColor(context, R.color.netspy_status_400);
        color300 = ContextCompat.getColor(context, R.color.netspy_status_300);

        cursorAdapter = new CursorAdapter(NetworkTabAdapter.this.context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.netspy_list_network_item, parent, false);
                ViewHolder holder = new ViewHolder(itemView);
                itemView.setTag(holder);
                return itemView;
            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                final HttpTransaction transaction = LocalCupboard.getInstance().withCursor(cursor).get(HttpTransaction.class);
                final ViewHolder holder = (ViewHolder) view.getTag();
                holder.path.setText(transaction.getMethod() + " " + transaction.getPath());
                holder.host.setText(transaction.getHost());
                holder.start.setText(transaction.getRequestStartTimeString());
                holder.ssl.setVisibility(transaction.isSsl() ? View.VISIBLE : View.GONE);
                if (transaction.getStatus() == HttpTransaction.Status.Complete) {
                    holder.code.setText(String.valueOf(transaction.getResponseCode()));
                    holder.duration.setText(transaction.getDurationString());
                    holder.size.setText(transaction.getTotalSizeString());
                } else {
                    holder.code.setText(null);
                    holder.duration.setText(null);
                    holder.size.setText(null);
                }
                if (transaction.getStatus() == HttpTransaction.Status.Failed) {
                    holder.code.setText("!!!");
                }
                setStatusColor(holder, transaction);
                holder.transaction = transaction;
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != NetworkTabAdapter.this.listener) {
                            NetworkTabAdapter.this.listener.onListFragmentInteraction(holder.transaction);
                        }
                    }
                });
            }

            private void setStatusColor(ViewHolder holder, HttpTransaction transaction) {
                int color;
                if (transaction.getStatus() == HttpTransaction.Status.Failed) {
                    color = colorError;
                } else if (transaction.getStatus() == HttpTransaction.Status.Requested) {
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
        };
    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursorAdapter.getCursor().moveToPosition(position);
        cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    void swapCursor(Cursor newCursor) {
        cursorAdapter.swapCursor(newCursor);
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
        public final ImageView ssl;
        HttpTransaction transaction;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            code = (TextView) view.findViewById(R.id.code);
            path = (TextView) view.findViewById(R.id.path);
            host = (TextView) view.findViewById(R.id.host);
            start = (TextView) view.findViewById(R.id.start);
            duration = (TextView) view.findViewById(R.id.duration);
            size = (TextView) view.findViewById(R.id.size);
            ssl = (ImageView) view.findViewById(R.id.ssl);
        }
    }
}