package com.creditease.netspy.inner.ui.apimock;

import java.io.Serializable;

/**
 * Created by zhxh on 2020/6/6
 * 数据表
 * url:http://10.106.156.200:5000/todos
 */
public class ApiMockData implements Serializable {
    private long time; //最近更新时间戳
    private int status; //返回数据类型 404 500 200等 1表示有数据 0表示无数据 -1表示错误
    private int from; //1表示 app 2表示web
    private boolean show; //请求时，只返回为true的resp
    private String method;
    private String path;
    private String host;
    private String port;//默认5000
    private String baseUrl;
    private String url;
    private String params;
    private String resp; //gson传
}
