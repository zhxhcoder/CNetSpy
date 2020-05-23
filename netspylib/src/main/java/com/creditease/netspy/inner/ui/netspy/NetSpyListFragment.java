package com.creditease.netspy.inner.ui.netspy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.creditease.netspy.R;
import com.creditease.netspy.DBHelper;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.support.NotificationHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            adapter = new NetSpyListAdapter(getContext(), listener);
            recyclerView.setAdapter(adapter);

            updateDataFromDb();
        }
        return view;
    }

    public void updateDataFromDb() {
        List<HttpEvent> dataList = DBHelper.getInstance().getAllHttpData();
        Collections.sort(dataList, (o1, o2) -> (int) (o2.getTransId() - o1.getTransId()));
        adapter.setData(filterText, dataList);
    }

    public void updateDataFromSearch() {
        List<HttpEvent> dataList = DBHelper.getInstance().queryHttpEventByFilter(filterText);
        Collections.sort(dataList, (o1, o2) -> (int) (o2.getTransId() - o1.getTransId()));
        adapter.setData(filterText, dataList);
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
    }
}
