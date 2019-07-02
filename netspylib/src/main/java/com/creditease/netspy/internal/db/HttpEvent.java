package com.creditease.netspy.internal.db;

import android.net.Uri;

import com.creditease.netspy.internal.support.FormatUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhxh on 2019/07/02
 * HttpEvent 实体类.
 */
@Entity
public class HttpEvent {

    //数据库表
    public static final String[] PARTIAL_PROJECTION = new String[]{
        "_id",
        "requestDate",
        "tookMs",
        "method",
        "host",
        "path",
        "scheme",
        "requestContentLength",
        "responseCode",
        "error",
        "responseContentLength"
    };

    public enum Status {
        Requested,
        Complete,
        Failed
    }

    @Id(autoincrement = true)
    private Long _id; // 自增id

    private java.util.Date requestDate;
    private java.util.Date responseDate;
    private Long tookMs;

    private String protocol;
    private String method;
    private String url;
    private String host;
    private String path;
    private String scheme;

    private Long requestContentLength;
    private String requestContentType;

    @Convert(converter = MapConverter.class, columnType = String.class)
    private Map<String, String> requestHeaders;
    private String requestBody;
    private boolean requestBodyIsPlainText = true;

    private Integer responseCode;
    private String responseMessage;
    private String error;

    private Long responseContentLength;
    private String responseContentType;

    @Convert(converter = MapConverter.class, columnType = String.class)
    private Map<String, String> responseHeaders;
    private String responseBody;
    private boolean responseBodyIsPlainText = true;

    @Keep()
    public HttpEvent(Long _id, java.util.Date requestDate,
                     java.util.Date responseDate, Long tookMs, String protocol,
                     String method, String url, String host, String path, String scheme,
                     Long requestContentLength, String requestContentType,
                     Map<String, String> requestHeaders, String requestBody,
                     boolean requestBodyIsPlainText, Integer responseCode,
                     String responseMessage, String error, Long responseContentLength,
                     String responseContentType, Map<String, String> responseHeaders, String responseBody,
                     boolean responseBodyIsPlainText) {
        this._id = _id;
        this.requestDate = requestDate;
        this.responseDate = responseDate;
        this.tookMs = tookMs;
        this.protocol = protocol;
        this.method = method;
        this.url = url;
        this.host = host;
        this.path = path;
        this.scheme = scheme;
        this.requestContentLength = requestContentLength;
        this.requestContentType = requestContentType;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
        this.requestBodyIsPlainText = requestBodyIsPlainText;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.error = error;
        this.responseContentLength = responseContentLength;
        this.responseContentType = responseContentType;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
        this.responseBodyIsPlainText = responseBodyIsPlainText;
    }

    @Keep()
    public HttpEvent() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public java.util.Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(java.util.Date requestDate) {
        this.requestDate = requestDate;
    }

    public java.util.Date getResponseDate() {
        return this.responseDate;
    }

    public void setResponseDate(java.util.Date responseDate) {
        this.responseDate = responseDate;
    }

    public Long getTookMs() {
        return this.tookMs;
    }

    public void setTookMs(Long tookMs) {
        this.tookMs = tookMs;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return this.url;
    }


    public void setHost(String host) {
        this.host = host;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Long getRequestContentLength() {
        return this.requestContentLength;
    }

    public void setRequestContentLength(Long requestContentLength) {
        this.requestContentLength = requestContentLength;
    }

    public String getRequestContentType() {
        return this.requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }


    public String getRequestBody() {
        return this.requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public boolean getRequestBodyIsPlainText() {
        return this.requestBodyIsPlainText;
    }

    public void setRequestBodyIsPlainText(boolean requestBodyIsPlainText) {
        this.requestBodyIsPlainText = requestBodyIsPlainText;
    }

    public Integer getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getResponseContentLength() {
        return this.responseContentLength;
    }

    public void setResponseContentLength(Long responseContentLength) {
        this.responseContentLength = responseContentLength;
    }

    public String getResponseContentType() {
        return this.responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }


    public Map<String, String> getResponseHeaders() {
        return this.responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> extras) {
        this.responseHeaders = extras;
    }

    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> extras) {
        this.requestHeaders = extras;
    }


    public String getResponseBody() {
        return this.responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public boolean getResponseBodyIsPlainText() {
        return this.responseBodyIsPlainText;
    }

    public void setResponseBodyIsPlainText(boolean responseBodyIsPlainText) {
        this.responseBodyIsPlainText = responseBodyIsPlainText;
    }

    /********************************************************/


    public void setUrl(String url) {
        this.url = url;
        Uri uri = Uri.parse(url);
        host = uri.getHost();
        path = uri.getPath() + ((uri.getQuery() != null) ? "?" + uri.getQuery() : "");
        scheme = uri.getScheme();
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public String getScheme() {
        return scheme;
    }


    public HttpEvent.Status getStatus() {
        if (error != null) {
            return HttpEvent.Status.Failed;
        } else if (responseCode == null) {
            return HttpEvent.Status.Requested;
        } else {
            return HttpEvent.Status.Complete;
        }
    }

    public String getRequestStartTimeString() {
        return (requestDate != null) ? requestDate.toString() : null;
    }

    public String getRequestDateString() {
        return (requestDate != null) ? requestDate.toString() : null;
    }

    public String getResponseDateString() {
        return (responseDate != null) ? responseDate.toString() : null;
    }

    public String getDurationString() {
        return (tookMs != null) ? +tookMs + " ms" : null;
    }

    public String getRequestSizeString() {
        return formatBytes((requestContentLength != null) ? requestContentLength : 0);
    }

    public String getResponseSizeString() {
        return (responseContentLength != null) ? formatBytes(responseContentLength) : null;
    }

    public String getTotalSizeString() {
        long reqBytes = (requestContentLength != null) ? requestContentLength : 0;
        long resBytes = (responseContentLength != null) ? responseContentLength : 0;
        return formatBytes(reqBytes + resBytes);
    }

    public String getResponseSummaryText() {
        switch (getStatus()) {
            case Failed:
                return error;
            case Requested:
                return null;
            default:
                return String.valueOf(responseCode) + " " + responseMessage;
        }
    }

    public String getNotificationText() {
        switch (getStatus()) {
            case Failed:
                return " ! ! !  " + path;
            case Requested:
                return " . . .  " + path;
            default:
                return String.valueOf(responseCode) + " " + path;
        }
    }

    public boolean isSsl() {
        return scheme.toLowerCase().equals("https");
    }


    private String formatBody(String body, String contentType) {
        if (contentType != null && contentType.toLowerCase().contains("json")) {
            return FormatUtils.formatJson(body);
        } else if (contentType != null && contentType.toLowerCase().contains("xml")) {
            return FormatUtils.formatXml(body);
        } else {
            return body;
        }
    }

    private String formatBytes(long bytes) {
        return FormatUtils.formatByteCount(bytes, true);
    }

    public String getFormattedRequestBody() {
        return formatBody(requestBody, requestContentType);
    }

    public String getFormattedResponseBody() {
        return formatBody(responseBody, responseContentType);
    }

}
