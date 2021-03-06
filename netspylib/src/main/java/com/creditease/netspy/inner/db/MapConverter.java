package com.creditease.netspy.inner.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhxh on 2019/07/02
 * 属性转换器
 */
public class MapConverter implements PropertyConverter<Map<String, String>, String> {
    private Gson gson;

    MapConverter() {
        gson = new Gson();
    }

    @Override
    public Map<String, String> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(databaseValue, type);
    }

    @Override
    public String convertToDatabaseValue(Map<String, String> entityProperty) {
        return gson.toJson(entityProperty);
    }
}
