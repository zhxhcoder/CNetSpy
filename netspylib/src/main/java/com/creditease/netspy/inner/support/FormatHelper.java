package com.creditease.netspy.inner.support;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.creditease.netspy.R;
import com.creditease.netspy.inner.db.HttpEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class FormatHelper {

    public static String formatHeaders(Map<String, String> httpHeaders, boolean withMarkup) {
        String out = "";
        if (httpHeaders != null) {
            for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
                out += ((withMarkup) ? "<b>" : "") + entry.getKey() + ": " + ((withMarkup) ? "</b>" : "") +
                        entry.getValue() + ((withMarkup) ? "<br />" : "\n");
            }
        }
        return out;
    }

    public static String formatByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String formatJson(String json) {
        try {
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(json);
            return JsonConverter.getInstance().toJson(je);
        } catch (Exception e) {
            return json;
        }
    }

    public static String formatXml(String xml) {
        try {
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
        } catch (Exception e) {
            return xml;
        }
    }

    /**
     * 仅仅返回 response信息
     *
     * @param context
     * @param transaction
     * @return
     */
    public static String getShareResponseText(Context context, HttpEvent transaction) {
        String text = "";
        text += (transaction.getResponseBodyIsPlainText()) ? v(transaction.getFormattedResponseBody()) :
                context.getString(R.string.netspy_body_omitted);
        return text;
    }

    public static String getShareCurlCommand(HttpEvent transaction) {
        boolean compressed = false;
        String curlCmd = "curl";
        curlCmd += " -X " + transaction.getMethod();
        Map<String, String> headers = transaction.getRequestHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if ("Accept-Encoding".equalsIgnoreCase(name) && "gzip".equalsIgnoreCase(value)) {
                compressed = true;
            }
            curlCmd += " -H " + "\"" + name + ": " + value + "\"";
        }
        String requestBody = transaction.getRequestBody();
        if (requestBody != null && requestBody.length() > 0) {
            // try to keep to a single line and use a subshell to preserve any line breaks
            curlCmd += " --data $'" + requestBody.replace("\n", "\\n") + "'";
        }
        curlCmd += ((compressed) ? " --compressed " : " ") + transaction.getMockUrl();
        return curlCmd;
    }

    private static String v(String string) {
        return (string != null) ? string : "";
    }


    public static String getHHmmSS(Date date) {
        String msg = "";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
        msg += sdf.format(date);
        return msg;
    }

    /**
     * 关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @return
     */
    public static SpannableString findSearch(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        if (TextUtils.isEmpty(keyword)) {
            return s;
        }
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    public static boolean isFindMatch(String text, String keyword) {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(text)) {
            return false;
        }
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(text);
        return m.find();
    }

    /**
     * 多个关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字数组
     * @return
     */
    public static SpannableString findSearch(int color, String text, String... keyword) {
        SpannableString s = new SpannableString(text);
        if (keyword == null || keyword.length == 0) {
            return s;
        }
        for (int i = 0; i < keyword.length; i++) {
            Pattern p = Pattern.compile(keyword[i]);
            Matcher m = p.matcher(s);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return s;
    }


    public static String timeStamp2Str(long timeStamp, String format) {
        if (timeStamp <= 0) {
            System.out.print("时间戳不能为空");
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 时间戳转成时间字符串
     * 例如： "2019-10-09 13:38:16" 转为 1570599496000
     *
     * @param dateStr "2019-10-09 13:38:16"
     * @param format  传空默认："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static long str2TimeStamp(String dateStr, String format) {
        if (dateStr == null || dateStr.length() == 0) {
            System.out.print("时间字符串不能为空");
            return 0l;
        }
        if (format == null || format.length() == 0) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            Date parseData = dateFormat.parse(dateStr);
            long timeStamp = parseData.getTime();
            return timeStamp;
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return 0l;
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
            return matches.get(0);
        }
        return "";
    }
}