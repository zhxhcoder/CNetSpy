package com.creditease.netspy.inner.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.support.FormatHelper;

/**
 * Created by zhxh on 2019/06/12
 */
public class NetworkOverviewFragment extends Fragment implements INetworkTabFragment {

    TextView url;
    TextView method;
    TextView protocol;
    TextView status;
    TextView response;
    TextView ssl;
    TextView requestTime;
    TextView responseTime;
    TextView duration;
    TextView requestSize;
    TextView responseSize;
    TextView totalSize;

    private HttpEvent httpEvent;

    public NetworkOverviewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.netspy_fragment_network_overview, container, false);
        url = view.findViewById(R.id.url);
        method = view.findViewById(R.id.method);
        protocol = view.findViewById(R.id.protocol);
        status = view.findViewById(R.id.status);
        response = view.findViewById(R.id.response);
        ssl = view.findViewById(R.id.ssl);
        requestTime = view.findViewById(R.id.request_time);
        responseTime = view.findViewById(R.id.response_time);
        duration = view.findViewById(R.id.duration);
        requestSize = view.findViewById(R.id.request_size);
        responseSize = view.findViewById(R.id.response_size);
        totalSize = view.findViewById(R.id.total_size);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateUI();
    }

    @Override
    public void httpTransUpdate(HttpEvent httpEvent) {
        this.httpEvent = httpEvent;
        populateUI();
    }

    private void populateUI() {
        if (isAdded() && httpEvent != null) {
            url.setText(httpEvent.getUrl());
            method.setText(httpEvent.getMethod());
            protocol.setText(httpEvent.getProtocol());
            status.setText(httpEvent.getStatus().toString());
            response.setText(httpEvent.getResponseSummaryText());
            ssl.setText((httpEvent.isSsl() ? R.string.netspy_yes : R.string.netspy_no));
            requestTime.setText(FormatHelper.getHHmmSS(httpEvent.getRequestDate()));
            responseTime.setText(FormatHelper.getHHmmSS(httpEvent.getResponseDate()));
            duration.setText(httpEvent.getDurationString());
            requestSize.setText(httpEvent.getRequestSizeString());
            responseSize.setText(httpEvent.getResponseSizeString());
            totalSize.setText(httpEvent.getTotalSizeString());
        }
    }
}
