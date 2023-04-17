package com.creditease.netspy.inner;

import android.text.TextUtils;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by zhxh on 2020/11/28
 */
public final class ApiMockInterceptor implements Interceptor {
    static boolean isApiMock = false;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private long maxContentLength = 250000L;

    public static Set<String> paramSet = new HashSet<String>() {{
        add("method");
        add("cardids");
        add("status");
        add("fundType");
        add("showPosition");
    }};

    public static void debug(boolean isApiMock) {
        ApiMockInterceptor.isApiMock = isApiMock;
    }

    public static boolean debug() {
        return isApiMock;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isApiMock) {//默认false 不拦截
            Request request = chain.request();
            return chain.proceed(request);
        }
        String strRequest = "";

        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        if (hasRequestBody && !bodyHasUnsupportedEncoding(request.headers())) {
            BufferedSource source = getNativeSource(new Buffer(), bodyGzipped(request.headers()));
            Buffer buffer = source.buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                strRequest = readFromBuffer(buffer, charset);
            }
        }

        //TODO 利用NetSpyInterceptor原来的数据
        StringBuilder pathParams = new StringBuilder();
        //针对的是data参数我gson传
        if (getDecodeRequest(strRequest).contains("data=")) {
            for (String param : paramSet) {
                String value = parseGsonValue(param, getDecodeRequest(strRequest));
                if (!TextUtils.isEmpty(value)) {
                    pathParams.append("--")
                            .append(param)
                            .append("--")
                            .append(value);
                }
            }
        }

        //针对是method等参数直接传值
        for (String param : paramSet) {
            if (getDecodeRequest(strRequest).contains(param + "=")) {
                pathParams.append("--")
                        .append(param)
                        .append("--")
                        .append(getParamValue(param, getDecodeRequest(strRequest)));
            }
        }

        //还要处理同一个接口 删除不相干参数 或者 统一加上一个特殊的参数
        //获取request的创建者builder
        HttpUrl oldHttpUrl = request.url();
        HttpUrl newHttpUrl = oldHttpUrl
                .newBuilder()
                .scheme("http")
                .host("10.24.119.254")
                .port(5000)
                .encodedPath("/" + oldHttpUrl.encodedPath().replace("/", "__").substring(2) + pathParams.toString())
                .build();
        Request.Builder builder = request.newBuilder();
        return chain.proceed(builder.url(newHttpUrl).build());

    }

    private BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }

    private boolean bodyHasUnsupportedEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null &&
                !contentEncoding.equalsIgnoreCase("identity") &&
                !contentEncoding.equalsIgnoreCase("gzip");
    }

    private boolean bodyGzipped(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    /**
     * 通过采样部分数据判断是否可读
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, maxContentLength);
        String body = "";
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
        }
        return body;
    }

    public String getDecodeRequest(String requestBody) {
        try {
            return URLDecoder.decode(requestBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return requestBody;
        }
    }

    //获得某个key的value值string类型
    public static String parseGsonValue(String key, CharSequence input) {
        if (TextUtils.isEmpty(input)) return "";
        if (TextUtils.isEmpty(key)) return "";
        List<String> matches = new ArrayList<>();

        String regex = "(?<=\"" + key + "\":\")[^\"]*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        if (matches.size() > 0) {
            return matches.get(0);
        }
        return "";
    }

    //获得某个key的value值string类型
    public static String getParamValue(String key, CharSequence input) {
        if (TextUtils.isEmpty(input)) return "";
        if (TextUtils.isEmpty(key)) return "";
        List<String> matches = new ArrayList<>();

        String regex = key + "=" + "[^&]*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        if (matches.size() > 0) {
            return matches.get(0).replace(key + "=", "");
        }
        return "";
    }
}