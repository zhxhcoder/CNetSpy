package com.creditease.netspy;

import android.content.Context;
import android.util.Log;

import com.creditease.netspy.inner.db.DBHelper;
import com.creditease.netspy.inner.db.HttpEvent;
import com.creditease.netspy.inner.support.NotificationHelper;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by zhxh on 2019/06/19
 * okhttp拦截器
 */
public final class NetSpyInterceptor implements Interceptor {
    private static final String LOG_TAG = "NetSpyInterceptor";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private final Context context;
    private final NotificationHelper notificationHelper;
    private boolean showNotification;
    private long maxContentLength = 250000L;


    /**
     * NetSpyInterceptor
     */
    public NetSpyInterceptor() {
        this.context = NetSpyHelper.netSpyApp;
        notificationHelper = new NotificationHelper(this.context);
        showNotification = NetSpyHelper.isNetSpy;
    }

    /**
     * 当有http时是否展示通知
     *
     * @param show true 展示通知, false 不展示
     * @return The {@link NetSpyInterceptor} instance.
     */
    public NetSpyInterceptor showNotification(boolean show) {
        showNotification = show;
        return this;
    }

    /**
     * 设置请求与返回最大长度（bytes）
     */
    public NetSpyInterceptor maxContentLength(long max) {
        this.maxContentLength = max;
        return this;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        if (!NetSpyHelper.isNetSpy) {//默认false 不拦截
            Request request = chain.request();
            return chain.proceed(request);
        }

        Request request = chain.request();

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        HttpEvent transaction = new HttpEvent();
        transaction.setSource(NetSpyHelper.source);
        transaction.setRequestDate(new Date());

        transaction.setMethod(request.method());


        if (request.method().equals("GET")) {
            transaction.setUrl(request.url().toString().replaceFirst("\\?.*$", ""));
            transaction.setRequestBody(request.url().toString().replaceFirst("^.+\\?", ""));
        } else {//POST
            transaction.setUrl(request.url().toString());
        }

        transaction.setRequestHeaders(toHttpHeaderMap(request.headers()));
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                transaction.setRequestContentType(requestBody.contentType().toString());
            }
            if (requestBody.contentLength() != -1) {
                transaction.setRequestContentLength(requestBody.contentLength());
            }
        }

        transaction.setRequestBodyIsPlainText(!bodyHasUnsupportedEncoding(request.headers()));
        if (hasRequestBody && transaction.getRequestBodyIsPlainText()) {
            BufferedSource source = getNativeSource(new Buffer(), bodyGzipped(request.headers()));
            Buffer buffer = source.buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                transaction.setRequestBody(readFromBuffer(buffer, charset));
            } else {
                transaction.setRequestBodyIsPlainText(false);
            }
        }

        long transId = createHttpEvent(transaction);

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            transaction.setError(e.toString());
            updateHttpEvent(transaction, transId);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();

        transaction.setRequestHeaders(toHttpHeaderMap(response.request().headers())); // includes headers added later in the chain
        transaction.setResponseDate(new Date());
        transaction.setTookMs(tookMs);
        transaction.setProtocol(response.protocol().toString());
        transaction.setResponseCode(response.code());
        transaction.setResponseMessage(response.message());

        transaction.setResponseContentLength(responseBody.contentLength());
        if (responseBody.contentType() != null) {
            transaction.setResponseContentType(responseBody.contentType().toString());
        }
        transaction.setResponseHeaders(toHttpHeaderMap(response.headers()));

        transaction.setResponseBodyIsPlainText(!bodyHasUnsupportedEncoding(response.headers()));
        if (HttpHeaders.hasBody(response) && transaction.getResponseBodyIsPlainText()) {
            BufferedSource source = getNativeSource(response);
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    updateHttpEvent(transaction, transId);
                    return response;
                }
            }
            if (isPlaintext(buffer)) {
                transaction.setResponseBody(readFromBuffer(buffer.clone(), charset));
            } else {
                transaction.setResponseBodyIsPlainText(false);
            }
            transaction.setResponseContentLength(buffer.size());
        }

        updateHttpEvent(transaction, transId);

        return response;
    }

    private long createHttpEvent(HttpEvent transaction) {
        transaction.setTransId(System.currentTimeMillis());

        DBHelper.getInstance().insertHttpData(transaction);

        if (showNotification) {
            notificationHelper.show(transaction);
        }
        return transaction.getTransId();
    }

    private long updateHttpEvent(HttpEvent transaction, long transId) {

        transaction.setTransId(transId);

        DBHelper.getInstance().insertHttpData(transaction);
        if (showNotification) {
            notificationHelper.show(transaction);
        }
        return transId;
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

    private String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, maxContentLength);
        String body = "";
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
            body += context.getString(R.string.netspy_body_unexpected_eof);
        }
        if (bufferSize > maxContentLength) {
            body += context.getString(R.string.netspy_body_content_truncated);
        }
        return body;
    }

    private BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }

    private BufferedSource getNativeSource(Response response) throws IOException {
        if (bodyGzipped(response.headers())) {
            BufferedSource source = response.peekBody(maxContentLength).source();
            if (source.buffer().size() < maxContentLength) {
                return getNativeSource(source, true);
            } else {
                Log.w(LOG_TAG, "gzip encoded response was too long");
            }
        }
        return response.body().source();
    }

    private Map<String, String> toHttpHeaderMap(Headers headers) {
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 0, count = headers.size(); i < count; i++) {
            headerMap.put(headers.name(i), headers.value(i));
        }
        return headerMap;
    }
}
