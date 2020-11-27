package com.creditease.netspy.inner.ui.bugspy;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creditease.netspy.inner.db.DBHelper;
import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.BugEvent;
import com.creditease.netspy.inner.support.OkHttpHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhxh on 2019/07/16
 */
public class BugSpyListFragment extends Fragment implements IBugTabFragment {

    private BugSpyListAdapter adapter;

    public BugSpyListFragment() {
    }

    public static BugSpyListFragment newInstance() {
        return new BugSpyListFragment();
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
            adapter = new BugSpyListAdapter(false, this, getContext());
            recyclerView.setAdapter(adapter);

            updateDataFromDb();
        }
        return view;
    }

    public void updateDataFromDb() {
        List<BugEvent> dataList = DBHelper.getInstance().getAllBugData();
        if (dataList.size() > 200) {
            AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                    .setTitle("温馨提示")
                    .setMessage("异常崩溃数据已经达到" + dataList.size() + "条，为防止数据过多请自动清理")
                    .setPositiveButton("清理", (dialog1, which) -> {
                        adapter.setData(new ArrayList<>());
                        DBHelper.getInstance().deleteAllBugData();
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        } else if (dataList.size() > 100) {
            Toast.makeText(getActivity(), "异常崩溃数据已经达到" + dataList.size() + "条，请按主动屏幕右上角删除按钮及时清理", Toast.LENGTH_LONG).show();
        }
        Collections.sort(dataList, (o1, o2) -> (int) (o2.getTimeStamp() - o1.getTimeStamp()));
        adapter.setData(dataList);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.netspy_bug, menu);
        MenuItem uploadMenuItem = menu.findItem(R.id.upload);
        uploadMenuItem.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.upload) {
            List<BugEvent> dataList = DBHelper.getInstance().getAllBugData();
            for (int i = 0; i < dataList.size(); i++) {
                BugEvent event = dataList.get(i);
                if (!TextUtils.isEmpty(event.getReport())) {
                    OkHttpHelper.getInstance().postBugRecords(event.getTimestamp(), event.getSummary(), event.getReport(), event.getDevice(), event.getUser(), event.getApp(), null);
                }
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateBugData(BugEvent bugEvent) {
        DBHelper.getInstance().deleteBugData(bugEvent);
        updateDataFromDb();
        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
    }
}
