package com.creditease.netspy.internal.ui;

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
import com.creditease.netspy.internal.db.HttpEvent;
import com.creditease.netspy.internal.support.FormatUtils;

/**
 * Created by zhxh on 2019/06/12
 */
public class NetworkResponseFragment extends Fragment implements NetworkTabFragment {

    public static final int TYPE_REQUEST = 0;
    public static final int TYPE_RESPONSE = 1;

    private static final String ARG_TYPE = "type";

    TextView headers;
    TextView body;

    private int type;
    private HttpEvent transaction;

    public NetworkResponseFragment() {
    }

    public static NetworkResponseFragment newInstance(int type) {
        NetworkResponseFragment fragment = new NetworkResponseFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_TYPE, type);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARG_TYPE);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.netspy_fragment_network_response, container, false);
        headers = view.findViewById(R.id.headers);
        body = view.findViewById(R.id.body);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateUI();
    }

    @Override
    public void transactionUpdated(HttpEvent transaction) {
        this.transaction = transaction;
        populateUI();
    }

    private void populateUI() {
        if (isAdded() && transaction != null) {
            switch (type) {
                case TYPE_REQUEST:
                    setText(FormatUtils.formatHeaders(transaction.getRequestHeaders(), true),
                        transaction.getFormattedRequestBody(), transaction.getRequestBodyIsPlainText());
                    break;
                case TYPE_RESPONSE:
                    setText(FormatUtils.formatHeaders(transaction.getResponseHeaders(), true),
                        transaction.getFormattedResponseBody(), transaction.getResponseBodyIsPlainText());
                    break;
            }
        }
    }

    private void setText(String headersString, String bodyString, boolean isPlainText) {
        headers.setVisibility((TextUtils.isEmpty(headersString) ? View.GONE : View.VISIBLE));
        headers.setText(Html.fromHtml(headersString));
        if (!isPlainText) {
            body.setText(getString(R.string.netspy_body_omitted));
        } else {
            body.setText(bodyString);
        }
    }
}