package com.creditease.netspy.inner.ui.apimock;

import java.io.Serializable;

/**
 * Created by zhxh on 2020/6/6
 * 数据表
 */
public class ApiMockData implements Serializable {
    public String path;
    public String show_type; // 1默认表示返回resp_data 0表示返回resp_empty -1表示返回resp_error
    public String resp_data; //gson传
    public String resp_empty; //gson传
    public String resp_error; //gson传
    public String timestamp; //时间

    public String getRespShow() {
        if ("-1".equals(show_type)) {
            return resp_error;
        } else if ("0".equals(show_type)) {
            return resp_empty;
        } else
            return resp_data;
    }

    public String getMockPath() {
        return path.replace("__", "/").replace("/api/item/","/");
    }

    public String getShowType() {
        if (show_type == null || "".equals(show_type))
            return "1";
        return show_type;
    }
}