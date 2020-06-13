package com.creditease.netspy.inner.ui.bugspy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.BugEvent;
import com.creditease.netspy.inner.db.DBHelper;
import com.creditease.netspy.inner.support.OkHttpHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhxh on 2019/07/16
 */
public class BugSpyRecordsFragment extends Fragment implements IBugTabFragment {
    List<BugEvent> dataList = new ArrayList<>();

    private BugSpyListAdapter adapter;

    public BugSpyRecordsFragment() {
    }

    public static BugSpyRecordsFragment newInstance() {
        return new BugSpyRecordsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.netspy_fragment_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));
            adapter = new BugSpyListAdapter(true, this, getContext());
            recyclerView.setAdapter(adapter);

            downLoadBugs();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.netspy_bug, menu);
        MenuItem uploadMenuItem = menu.findItem(R.id.upload);
        uploadMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.upload) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void downLoadBugs() {
        OkHttpHelper.getInstance().getBugRecords(handler);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == OkHttpHelper.LIST_SUCCESS) {
                try {
                    dataList = new Gson().fromJson(String.valueOf(msg.obj), new TypeToken<List<BugEvent>>() {
                    }.getType());
                    if (dataList == null || dataList.isEmpty()) {
                        return;
                    }
                    Collections.sort(dataList, (o1, o2) -> (int) (o2.getTimeStamp() - o1.getTimeStamp()));
                    adapter.setData(dataList);
                } catch (JsonSyntaxException e) {
                    dataList = new ArrayList<>();
                    e.printStackTrace();
                }
            } else if (msg.what == OkHttpHelper.DEL_SUCCESS) {
                downLoadBugs();
                Toast.makeText(getActivity(), "删除成功,重新请求数据", Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void updateBugData(BugEvent bugEvent) {
        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle("FBI警告")
                .setMessage("此操作将同时删除远程服务器上的Bug记录，删除后其他人将无法查阅，请慎重")
                .setPositiveButton("放弃", null)
                .setNegativeButton("任性", (dialog1, which) -> OkHttpHelper.getInstance().deleteBugItem(handler))
                .create();
        dialog.show();

    }
}
