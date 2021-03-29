package com.creditease.netspy;

import android.content.Context;
import android.content.Intent;

import com.creditease.netspy.inner.ui.apimock.ApiMockListActivity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhxh on 2020/6/6
 */
public final class ApiMockHelper {
    static boolean isApiMock = false;
    //默认baseUrl
    private static String baseURL = "http://10.106.157.94:5000/";

    private static Set<String> paramSet = new HashSet<String>() {{
        add("method");
        add("cardids");
        add("status");
        add("fundType");
        add("showPosition");
    }};

    public static void initBaseURL(String baseURL) {
        ApiMockHelper.baseURL = baseURL;
    }

    public static String getBaseURL() {
        if (baseURL == null || baseURL.isEmpty()) {
            return "";
        }
        return baseURL;
    }

    public static void initParamSet(String strParamSet) {
        if (strParamSet == null || strParamSet.isEmpty()) {
            return;
        }
        paramSet = new HashSet<String>();
        for (String s : strParamSet.split("\\s*,\\s*")) {
            addParams(s);
        }
    }


    public static Set<String> getParamSet() {
        return paramSet;
    }

    public static void addParams(String param) {
        if (paramSet == null) {
            paramSet = new HashSet<String>();
        }
        paramSet.add(param);
    }

    public static void debug(boolean isApiMock) {
        ApiMockHelper.isApiMock = isApiMock;
    }

    public static boolean debug() {
        return ApiMockHelper.isApiMock;
    }


    public static void launchActivity(Context context) {
        if (ApiMockHelper.isApiMock) {
            context.startActivity(new Intent(context, ApiMockListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            return;
        }
    }

    public static String getHost() {
        try {
            URI uri = new URI(baseURL);
            return uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMockUrl(String reBaseUrl, String url, Map<String, String> params) {
        StringBuilder pathParams = new StringBuilder();
        //针对是method等参数直接传值
        for (String param : ApiMockHelper.getParamSet()) {
            for (String key : params.keySet()) {
                if (param.equals(key)) {
                    pathParams.append("--")
                            .append(param)
                            .append("--")
                            .append(params.get(key));
                }
            }
        }
        String urlPost = url.replace(reBaseUrl + "/", "");
        urlPost = urlPost.replace("/", "__");
        return ApiMockHelper.getBaseURL() + urlPost + pathParams.toString();
    }

    public static String getMockPath(String reBaseUrl, String url) {
        String urlPost = url.replace(reBaseUrl + "/", "");
        urlPost = urlPost.replaceFirst("\\?.+$", "");
        urlPost = urlPost.replace("/", "__");
        return urlPost;
    }

}