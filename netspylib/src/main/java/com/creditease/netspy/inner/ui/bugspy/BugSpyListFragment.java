package com.creditease.netspy.inner.ui.bugspy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.creditease.netspy.DBHelper;
import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.BugEvent;
import com.creditease.netspy.inner.db.HttpEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhxh on 2019/07/16
 */
public class BugSpyListFragment extends Fragment {

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
            adapter = new BugSpyListAdapter(this, getContext());
            recyclerView.setAdapter(adapter);

            updateDataFromDb();
        }
        return view;
    }

    public void updateDataFromDb() {
        List<BugEvent> dataList = DBHelper.getInstance().getAllBugData();
        if (dataList.size() > 200) {
            Toast.makeText(getActivity(), "异常崩溃数据已经达到" + dataList.size() + "条，为防止数据过多已经自动清理", Toast.LENGTH_LONG).show();
            adapter.setData(new ArrayList<>());
            DBHelper.getInstance().deleteAllBugData();
            return;
        } else if (dataList.size() > 100) {
            Toast.makeText(getActivity(), "异常崩溃数据已经达到" + dataList.size() + "条，请按主动屏幕右上角删除按钮及时清理（当数据超过200条会触发自动清理）", Toast.LENGTH_LONG).show();
        }
        Collections.sort(dataList, (o1, o2) -> (int) (o2.getTimeStamp() - o1.getTimeStamp()));
        adapter.setData(dataList);
    }

    public void updateDataFromDelete(BugEvent bugEvent) {
        DBHelper.getInstance().deleteBugData(bugEvent);
        updateDataFromDb();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.netspy_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            adapter.setData(new ArrayList<>());
            DBHelper.getInstance().deleteAllBugData();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
