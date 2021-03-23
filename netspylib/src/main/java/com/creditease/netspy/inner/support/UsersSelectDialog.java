package com.creditease.netspy.inner.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.UsersItemData;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxh on 2020/8/19
 */
public class UsersSelectDialog extends FrameLayout {
    List<UsersItemData> dataList = new ArrayList<>();

    Activity activity;
    View rootView;
    View contentLayout;
    View ivDialogClose;
    LinearLayout llContainer;

    OnSelectListener selectListener;

    public UsersSelectDialog(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public UsersSelectDialog(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public UsersSelectDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        rootView = LayoutInflater.from(context).inflate(R.layout.users_select_layout, this);
        contentLayout = rootView.findViewById(R.id.contentLayout);
        ivDialogClose = rootView.findViewById(R.id.ivDialogClose);
        llContainer = rootView.findViewById(R.id.llContainer);
    }

    public void requestUsers(String source, OnSelectListener selectListener) {
        this.selectListener = selectListener;
        OkHttpHelper.getInstance().getUsersRecords(source, handler);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == OkHttpHelper.LIST_SUCCESS) {
                try {
                    dataList = new Gson().fromJson(String.valueOf(msg.obj), new TypeToken<List<UsersItemData>>() {
                    }.getType());
                    if (dataList == null || dataList.isEmpty()) {
                        dataList = new ArrayList<>();
                    }
                    show(dataList, selectListener);
                } catch (JsonSyntaxException e) {
                    dataList = new ArrayList<>();
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        }
    };

    public void show(List<UsersItemData> dataList, OnSelectListener submitListener) {
        if (activity == null) {
            return;
        }
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        llContainer.setVisibility(VISIBLE);
        llContainer.removeAllViews();

        for (int i = 0; i < dataList.size(); i++) {
            UsersItemData data = dataList.get(i);
            View itemView = LayoutInflater.from(activity).inflate(R.layout.users_select_item, null);
            TextView title = itemView.findViewById(R.id.title);
            TextView time = itemView.findViewById(R.id.time);

            title.setText("应用：" + data.getSource() + " 环境：" + data.getFlavor() + " 用户名：" + data.getName());
            time.setText(data.update_time_show);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitListener.onUser(data.getName(), data.getPwd());
                    close();
                }
            });

            llContainer.addView(itemView);
        }

        contentLayout.setOnClickListener(view -> {
        });
        // 解决崩溃问题
        if (this.getParent() != null) {
            ((ViewGroup) (this.getParent())).removeView(this);
        }
        // 在当前Activity加入本View
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        activity.addContentView(this, layoutParams);

        this.setVisibility(View.VISIBLE);
        this.setClickable(true);


        ivDialogClose.setOnClickListener(v -> close());
    }


    private void close() {
        ((ViewGroup) (activity.getWindow().getDecorView().findViewById(android.R.id.content))).removeView(this);
        this.setVisibility(View.GONE);
    }

    public interface OnSelectListener {
        void onUser(String name, String pwd);
    }

}
