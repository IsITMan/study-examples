package com.wangms.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author: wangms
 * @date: 2019-03-26 13:35
 */
public class ApacheHttpClient {
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int SO_TIMEOUT = 10000;
    private static Logger logger = LoggerFactory.getLogger(ApacheHttpClient.class);

    /**
     * 获得用户远程地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (StringUtils.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 转换请求的所有参数为字符串
     *
     * @param request
     * @return
     */
    public static String convertParamtersToStr(HttpServletRequest request) {
        Map<String, String[]> paramterMap = request.getParameterMap();
        String strParamters = "";

        if (paramterMap.size() > 0) {
            Map<String, List<String>> paramterListMap = new HashMap<String, List<String>>();
            for (Entry<String, String[]> paramter : paramterMap.entrySet()) {
                paramterListMap.put(paramter.getKey(), Arrays.asList(paramter.getValue()));
            }

            strParamters = paramterListMap.toString();
        }

        return strParamters;
    }

    public static String doPost(String url, String json, CookieStore cookieStore) throws IOException {
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        if (json != null) {
            httppost.setEntity(new StringEntity(json, Charset.forName("UTF-8")));
        }
        return getContent(httppost, cookieStore);
    }


    private static List<NameValuePair> getParamList(Map<String, String> params) {
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        for (Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue().split(",");
            for (String value : values) {
                paramList.add(new BasicNameValuePair(key, value.trim()));
            }
        }
        return paramList;
    }

    public static CloseableHttpResponse doPostResponse(String url, List<NameValuePair> params, CookieStore cookieStore) throws IOException {
        HttpPost httppost = new HttpPost(url);

        if (params != null) {
            httppost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF8")));
        }
        return getResponse(httppost, cookieStore);
    }

    public static CloseableHttpResponse doGet(String url, CookieStore cookieStore) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return getResponse(httpGet, cookieStore);
    }

    public static CloseableHttpResponse getResponse(HttpUriRequest request, CookieStore cookieStore) throws ClientProtocolException, IOException {
        CloseableHttpClient client = getDefaultClient(cookieStore);
        CloseableHttpResponse response = client.execute(request);
        logger.debug("=====>> response status: {}", response.getStatusLine());
        return response;
    }

    public static String doPost(String url, Map<String, String> params, CookieStore cookieStore) throws IOException {

        return doPost(url, getParamList(params), cookieStore);
    }

    public static String doPost(String url, List<NameValuePair> params, CookieStore cookieStore) throws IOException {

        HttpPost httppost = new HttpPost(url);

        if (params != null) {
            httppost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF8")));
        }

        return getContent(httppost, cookieStore);
    }

    public static String doGet(String url, List<NameValuePair> params, CookieStore cookieStore) throws IOException {

        HttpGet httpGet = new HttpGet(url);


        return getContent(httpGet, cookieStore);
    }

    private static String getContent(HttpUriRequest request, CookieStore cookieStore) throws IOException {

        CloseableHttpClient client = getDefaultClient(cookieStore);

        String content = null;
        try {
            CloseableHttpResponse response = client.execute(request);
            logger.debug("=====>> response status: {}", response.getStatusLine());
            try {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    try {
                        content = EntityUtils.toString(entity, "UTF-8");
                    } catch (IOException ex) {
                        logger.error("=====>> HTTP请求失败！", ex);
                        throw ex;
                    }
                }

                if (content != null) {
                    logger.debug("=====>> response content: {}", content.substring(0, Math.min(1024 * 2, content.length())));
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }

        return content;
    }

    public static CloseableHttpClient getDefaultClient() {
        return getDefaultClient(null);
    }

    public static CloseableHttpClient getDefaultClient(CookieStore cookieStore) {
        List<Header> headers = new ArrayList();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36"));

        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
                .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT)
                .build();

        HttpClientBuilder builder = HttpClients.custom()
                .setDefaultRequestConfig(globalConfig)
                .setDefaultHeaders(headers);
        if (cookieStore != null) {
            builder.setDefaultCookieStore(cookieStore);
        }
        return builder.build();
    }


    public static void main(String[] args) throws IOException {
        String str = doGet("http://v.juhe.cn/xhzd/query?key=&word=%E8%81%9A", null, null);
        System.out.println(str);
        Map map = GsonTool.fromJson(str, Map.class);
        System.out.println(map.get("reason").toString());
    }
}
