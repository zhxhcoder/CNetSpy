package com.creditease.netspy.inner.ui.netspy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.support.DeviceInfoHelper;
import com.creditease.netspy.inner.support.FormatHelper;

/**
 * Created by zhxh on 2019/06/12
 */
public class HttpOverviewFragment extends Fragment implements IHttpTabFragment {

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

    TextView responseHeaders;
    TextView device;


    private HttpEvent httpEvent;

    public HttpOverviewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.netspy_fragment_http_overview, container, false);
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

        responseHeaders = view.findViewById(R.id.responseHeaders);
        device = view.findViewById(R.id.device);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateUI();
    }

    @Override
    public void httpTransUpdate(String filterText, HttpEvent httpEvent) {
        this.httpEvent = httpEvent;
        populateUI();
    }

    private void populateUI() {
        if (isAdded() && httpEvent != null) {
            url.setText(httpEvent.getMockUrl());
            method.setText(httpEvent.getMethod());
            protocol.setText(httpEvent.getProtocol());
            status.setText(httpEvent.getStatus().toString());
            response.setText(httpEvent.getResponseSummaryText());
            ssl.setText((httpEvent.isSsl() ? R.string.netspy_yes : R.string.netspy_no));
            requestTime.setText(FormatHelper.getHHmmSS(httpEvent.getRequestDate()));

            if (httpEvent.getResponseDate() != null) {
                responseTime.setText(FormatHelper.getHHmmSS(httpEvent.getResponseDate()));
            }
            duration.setText(httpEvent.getDurationString());
            requestSize.setText(httpEvent.getRequestSizeString());
            responseSize.setText(httpEvent.getResponseSizeString());
            totalSize.setText(httpEvent.getTotalSizeString());

            setResponseHeader(FormatHelper.formatHeaders(httpEvent.getResponseHeaders(), true));

            device.setText(DeviceInfoHelper.getInstance().getAllDeviceInfo(getActivity()));
        }
    }

    private void setResponseHeader(String headersString) {
        responseHeaders.setVisibility((TextUtils.isEmpty(headersString) ? View.GONE : View.VISIBLE));
        responseHeaders.setText(Html.fromHtml(headersString));
    }
}
