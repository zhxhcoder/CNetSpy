package com.creditease.netspy.inner.ui.netspy;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

import com.creditease.netspy.ApiMockHelper;
import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.DBHelper;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.support.NotificationHelper;
import com.creditease.netspy.inner.support.OkHttpHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by zhxh on 2019/06/12
 * 请求列表
 */
public class NetSpyListFragment extends Fragment implements
        SearchView.OnQueryTextListener {

    private String filterText = "";
    private OnListFragmentInteractionListener listener;
    private NetSpyListAdapter adapter;

    public NetSpyListFragment() {
    }

    public static NetSpyListFragment newInstance() {
        return new NetSpyListFragment();
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
            adapter = new NetSpyListAdapter(this, getContext(), listener);
            recyclerView.setAdapter(adapter);

            updateDataFromDb();
        }
        return view;
    }

    public void updateDataFromDb() {
        List<HttpEvent> dataList = DBHelper.getInstance().getAllHttpData();
        if (dataList.size() > 500) {
            AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                    .setTitle("温馨提示")
                    .setMessage("请求接口数据已经达到" + dataList.size() + "条，为防止数据过多请自动清理")
                    .setPositiveButton("清理", (dialog1, which) -> {
                        adapter.setData(filterText, new ArrayList<>());
                        DBHelper.getInstance().deleteAllHttpData();
                        NotificationHelper.clearBuffer();
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        } else if (dataList.size() > 200) {
            Toast.makeText(getActivity(), "请求接口数据已经达到" + dataList.size() + "条，请按主动屏幕右上角删除按钮及时清理", Toast.LENGTH_LONG).show();
        }
        Collections.sort(dataList, (o1, o2) -> (int) (o2.getTransId() - o1.getTransId()));
        adapter.setData(filterText, dataList);
    }

    public void updateDataFromSearch() {
        List<HttpEvent> dataList = DBHelper.getInstance().queryHttpEventByFilter(filterText);
        Collections.sort(dataList, (o1, o2) -> (int) (o2.getTransId() - o1.getTransId()));
        adapter.setData(filterText, dataList);
    }

    public void updateDataFromDelete(HttpEvent httpEvent) {
        DBHelper.getInstance().deleteHttpData(httpEvent);
        updateDataFromDb();
        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
    }

    private void removeDuplicateFromDb() {
        List<HttpEvent> dataList = DBHelper.getInstance().getAllHttpData();
        Set<String> pathMethodCodeSet = new HashSet<>();
        for (int i = 0; i < dataList.size(); i++) {
            HttpEvent event = dataList.get(i);
            String pathMethodCodeStr = event.getPathWithParam() + event.getMethod() + event.getResponseCode();
            if (!pathMethodCodeSet.contains(pathMethodCodeStr)) {//去重
                pathMethodCodeSet.add(pathMethodCodeStr);
            } else {
                DBHelper.getInstance().deleteHttpData(event);
            }
        }

        updateDataFromDb();
        Toast.makeText(getActivity(), "去重成功", Toast.LENGTH_SHORT).show();
    }

    public void uploadAllCloudFromDb() {
        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle("温馨提示")
                .setMessage("将上传所有接口相关数据到服务器，并可能覆盖服务器上相同接口的相关数据")
                .setPositiveButton("上传", (dialog1, which) -> {
                    //Todo pathSet去重
                    List<HttpEvent> dataList = DBHelper.getInstance().getAllHttpData();
                    Set<String> pathSet = new HashSet<>();
                    for (int i = 0; i < dataList.size(); i++) {
                        HttpEvent event = dataList.get(i);
                        String pathStr = event.getPathWithParam();
                        if (!TextUtils.isEmpty(event.getResponseBody()) && !ApiMockHelper.getHost().equals(event.getHost()) && !pathSet.contains(pathStr)) {//本来就是服务器上的数据不再上传
                            pathSet.add(pathStr);
                            OkHttpHelper.getInstance().postApiRecords(event.getSource(), pathStr, 1, event.getResponseBody(), "", "", null);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    public void uploadCloudFromDb(HttpEvent event) {
        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle("温馨提示")
                .setMessage("将上传接口：" + event.getMockPath() + "\n到服务器，并可能覆盖服务器上该接口的相关数据")
                .setPositiveButton("上传", (dialog1, which) -> {
                    String pathStr = event.getPathWithParam();
                    if (!TextUtils.isEmpty(event.getResponseBody()) && !ApiMockHelper.getHost().equals(event.getHost())) {//本来就是服务器上的数据不再上传
                        OkHttpHelper.getInstance().postApiRecords(event.getSource(), pathStr, 1, event.getResponseBody(), "", "", null);
                    }

                    event.setUploaded(true);
                    DBHelper.getInstance().updateHttpData(event);
                    updateDataFromDb();
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.netspy_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            return true;
        } else if (item.getItemId() == R.id.remove) {
            removeDuplicateFromDb();
            return true;
        } else if (item.getItemId() == R.id.upload) {
            uploadAllCloudFromDb();
            return true;
        } else if (item.getItemId() == R.id.clear) {
            adapter.setData(filterText, new ArrayList<>());
            DBHelper.getInstance().deleteAllHttpData();
            NotificationHelper.clearBuffer();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterText = newText;
        if (TextUtils.isEmpty(filterText)) {
            updateDataFromDb();
        } else {
            updateDataFromSearch();
        }
        return true;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(HttpEvent item);

        void onRefreshTitle(String leftTitle);
    }
}
