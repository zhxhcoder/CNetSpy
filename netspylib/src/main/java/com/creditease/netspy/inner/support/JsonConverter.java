package com.creditease.netspy.inner.support;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

/**
 * Created by zhxh on 2019/06/12
 */
public class JsonConverter {

    private static Gson gson = null;

    private JsonConverter() {
    }

    public static Gson getInstance() {
        if (gson == null) {
            gson = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        }
        return gson;
    }
}
