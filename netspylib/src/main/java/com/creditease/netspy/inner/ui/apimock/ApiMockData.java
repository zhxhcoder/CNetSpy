package com.creditease.netspy.inner.ui.apimock;

import java.io.Serializable;

/**
 * Created by zhxh on 2020/6/6
 * 数据表
 */
public class ApiMockData implements Serializable {
    private long timeStamp;
    private int fromType;
    private String method;
    private String path;
    private String host;
    private String baseUrl;
    private String url;
    private String params;
    private String resp;
}
