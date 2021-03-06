package com.creditease.netspy.inner.ui.netspy;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
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
public class HttpResponseFragment extends Fragment implements IHttpTabFragment {
    private String filterText = "";

    TextView requestHeaders;
    TextView requestUrl;
    TextView requestBody;
    TextView responseBody;
    TextView responseRaw;

    private HttpEvent httpEvent;

    public HttpResponseFragment() {
    }

    public static HttpResponseFragment newInstance() {
        HttpResponseFragment fragment = new HttpResponseFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.netspy_fragment_http_response, container, false);
        requestHeaders = view.findViewById(R.id.requestHeaders);
        requestUrl = view.findViewById(R.id.requestUrl);
        requestBody = view.findViewById(R.id.requestBody);
        responseBody = view.findViewById(R.id.responseBody);
        responseRaw = view.findViewById(R.id.responseRaw);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyUI();
    }

    @Override
    public void httpTransUpdate(String filterText, HttpEvent httpEvent) {
        this.filterText = filterText;
        this.httpEvent = httpEvent;
        applyUI();
    }

    private void applyUI() {
        if (isAdded() && httpEvent != null) {
            responseRaw.setOnClickListener(v -> {
                responseRaw.setTextColor(Color.BLUE);
                setResponseText(httpEvent.getResponseBody(), true);
            });

            requestUrl.setText(String.format("%s %s", httpEvent.getMethod(), httpEvent.getMockUrl()));

            setRequestText(FormatHelper.formatHeaders(httpEvent.getRequestHeaders(), true),
                    httpEvent.getFormattedRequestBody(), httpEvent.getRequestBodyIsPlainText());

            setResponseText(httpEvent.getFormattedResponseBody(), httpEvent.getResponseBodyIsPlainText());
        }
    }

    private void setRequestText(String headersString, String bodyString, boolean isPlainText) {
        requestHeaders.setVisibility((TextUtils.isEmpty(headersString) ? View.GONE : View.VISIBLE));
        requestHeaders.setText(Html.fromHtml(headersString));
        if (!isPlainText) {
            requestBody.setText(getString(R.string.netspy_body_omitted));
        } else {
            requestBody.setText(bodyString);
        }
    }

    private void setResponseText(String bodyString, boolean isPlainText) {
        if (!isPlainText) {
            responseBody.setText(getString(R.string.netspy_body_omitted));
        } else {
            if (TextUtils.isEmpty(filterText) || getContext() == null) {
                responseBody.setText(bodyString);
            } else {
                responseBody.setText(FormatHelper.findSearch(Color.BLUE, bodyString, filterText));
            }
        }
    }
}