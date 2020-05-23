package com.creditease.netspy.inner.ui.netspy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateUI();
    }

    @Override
    public void httpTransUpdate(String filterText, HttpEvent httpEvent) {
        this.filterText = filterText;
        this.httpEvent = httpEvent;
        populateUI();
    }

    private void populateUI() {
        if (isAdded() && httpEvent != null) {
            requestUrl.setText(String.format("%s %s", httpEvent.getMethod(), httpEvent.getUrl()));

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
                responseBody.setText(FormatHelper.findSearch(ContextCompat.getColor(getContext(), R.color.netspy_status_500), bodyString, filterText));
            }
        }
    }
}