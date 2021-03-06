package com.creditease.netspy.inner.db;

import android.net.Uri;
import android.text.TextUtils;

import com.creditease.netspy.ApiMockHelper;
import com.creditease.netspy.inner.support.FormatHelper;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhxh on 2019/07/02
 * HttpEvent 实体类.
 */
@Entity
public class HttpEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        Requested,
        Complete,
        Failed
    }

    @Id(autoincrement = true)
    private Long _id; // 自增id
    private Long transId; // 业务id

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
    private boolean isUploaded = false;
    private String source;

    @Keep()
    public HttpEvent(Long _id, Long transId, java.util.Date requestDate,
                     java.util.Date responseDate, Long tookMs, String protocol,
                     String method, String url, String host, String path, String scheme,
                     Long requestContentLength, String requestContentType,
                     Map<String, String> requestHeaders, String requestBody,
                     boolean requestBodyIsPlainText, Integer responseCode,
                     String responseMessage, String error, Long responseContentLength,
                     String responseContentType, Map<String, String> responseHeaders, String responseBody,
                     boolean responseBodyIsPlainText, boolean isUploaded, String source) {
        this._id = _id;
        this.transId = transId;
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
        this.isUploaded = isUploaded;
        this.source = source;
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

    public Long getTransId() {
        return transId;
    }

    public void setTransId(Long transId) {
        this.transId = transId;
    }

    public java.util.Date getRequestDate() {
        if (this.requestDate == null) {
            return new java.util.Date();
        }
        return this.requestDate;
    }

    public void setRequestDate(java.util.Date requestDate) {
        this.requestDate = requestDate;
    }

    public java.util.Date getResponseDate() {
        if (this.responseDate == null) {
            return new java.util.Date();
        }
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

    public String getMockUrl() {
        if (ApiMockHelper.getHost().equals(getHost())) {
            return this.url.replace("__", "/");
        } else {
            return this.url;
        }
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
        if (requestBody == null) {
            return "";
        }
        try {
            return URLDecoder.decode(requestBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return this.requestBody;
        }
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
        if (responseCode == null) {
            return 0;
        }
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

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
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

    //TODO 获取带有特殊参数
    public String getPathWithParam() {
        StringBuilder pathParams = new StringBuilder();
        //针对的是data参数我gson传
        if (getRequestBody().contains("data=")) {
            for (String param : ApiMockHelper.getParamSet()) {
                String value = FormatHelper.parseGsonValue(param, getRequestBody());
                if (!TextUtils.isEmpty(value)) {
                    pathParams.append("--")
                            .append(param)
                            .append("--")
                            .append(value);
                }
            }
        }

        //针对是method等参数直接传值
        for (String param : ApiMockHelper.getParamSet()) {
            if (getRequestBody().contains(param + "=")) {
                pathParams.append("--")
                        .append(param)
                        .append("--")
                        .append(getParamValue(param));
            }
        }
        return path + pathParams;
    }

    private String getParamValue(String param) {
        return FormatHelper.getParamValue(param, getRequestBody());
    }

    public String getMockPath() {
        if (ApiMockHelper.getHost().equals(getHost())) {
            if (path.startsWith("/")) {
            } else {
            }
            return path.replace("__", "/");
        } else {
            return path;
        }
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
                return " ! ! !  " + getMockPath();
            case Requested:
                return " . . .  " + getMockPath();
            default:
                return String.valueOf(responseCode) + " " + getMockPath();
        }
    }

    public boolean isSsl() {
        return scheme.toLowerCase().equals("https");
    }


    private String formatBody(String body, String contentType) {
        if (contentType != null && contentType.toLowerCase().contains("json")) {
            return FormatHelper.formatJson(body);
        } else if (contentType != null && contentType.toLowerCase().contains("xml")) {
            return FormatHelper.formatXml(body);
        } else {
            return body;
        }
    }

    private String formatBytes(long bytes) {
        return FormatHelper.formatByteCount(bytes, true);
    }

    public String getFormattedRequestBody() {
        return formatBody(getRequestBody(), requestContentType);
    }

    public String getFormattedResponseBody() {
        return formatBody(responseBody, responseContentType);
    }

    @Override
    public String toString() {
        return "HttpEvent{" +
                "_id=" + _id +
                ", transId=" + transId +
                ", requestDate=" + requestDate +
                ", responseDate=" + responseDate +
                ", tookMs=" + tookMs +
                ", protocol='" + protocol + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", host='" + host + '\'' +
                ", path='" + path + '\'' +
                ", scheme='" + scheme + '\'' +
                ", requestContentLength=" + requestContentLength +
                ", requestContentType='" + requestContentType + '\'' +
                ", requestHeaders=" + requestHeaders +
                ", requestBody='" + getRequestBody() + '\'' +
                ", requestBodyIsPlainText=" + requestBodyIsPlainText +
                ", responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", error='" + error + '\'' +
                ", responseContentLength=" + responseContentLength +
                ", responseContentType='" + responseContentType + '\'' +
                ", responseHeaders=" + responseHeaders +
                ", responseBody='" + responseBody + '\'' +
                ", responseBodyIsPlainText=" + responseBodyIsPlainText +
                ", isUploaded=" + isUploaded +
                ", source=" + source +
                '}';
    }

    public boolean getIsUploaded() {
        return this.isUploaded;
    }

    public void setIsUploaded(boolean isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
