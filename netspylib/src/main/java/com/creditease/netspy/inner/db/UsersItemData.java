package com.creditease.netspy.inner.db;

import java.io.Serializable;

/**
 * Created by zhxh on 2020/6/6
 * 数据表
 */
public class UsersItemData implements Serializable {
    private String name;
    private String pwd; //
    private String source;
    public String flavor; //gson传
    public String feature; //gson传
    public String desc; //gson传
    public String timestamp; //时间
    public String update_time_show; //时间


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUpdate_time_show() {
        return update_time_show;
    }

    public void setUpdate_time_show(String update_time_show) {
        this.update_time_show = update_time_show;
    }
}