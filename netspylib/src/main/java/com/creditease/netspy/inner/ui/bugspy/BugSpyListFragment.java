package com.creditease.netspy.inner.ui.bugspy;

import android.content.Context;
import android.os.Bundle;
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

import com.creditease.netspy.inner.db.DBHelper;
import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.BugEvent;

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
            adapter.setData(new ArrayList<>());
            DBHelper.getInstance().deleteAllBugData();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateBugData(BugEvent bugEvent) {
        DBHelper.getInstance().deleteBugData(bugEvent);
        updateDataFromDb();
    }
}
