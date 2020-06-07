package com.creditease.netspy.inner.ui.apimock;

import java.io.Serializable;

/**
 * Created by zhxh on 2020/6/6
 * 数据表
 * url:http://10.106.156.200:5000/todos
 */
public class ApiMockData implements Serializable {
    private int status; //返回数据类型 404 500 200等 1表示有数据 0表示无数据 -1表示错误
    private int source; //1表示 app 2表示web
    private int show; // 1默认表示返回resp_data 0表示返回resp_empty -1表示返回resp_error
    private String time; //最近更新时间戳
    private String method;
    private String path;
    private String host;
    private String port;//默认5000
    private String base_url;
    private String url;
    private String params;
    private String request_info;
    private String resp_data; //gson传 默认1
    private String resp_empty; //gson传
    private String resp_error; //gson传
}