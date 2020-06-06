package com.creditease.netspy;

/**
 * Created by zhxh on 2020/6/6
 */
public final class ApiMockHelper {

    //默认baseUrl
    private static String baseUrl = "http://localhost.com";

    public static void initBaseUrl(String baseUrl) {
        ApiMockHelper.baseUrl = baseUrl;
    }
}