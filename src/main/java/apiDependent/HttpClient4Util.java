package apiDependent;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author wangx
 * @Title: HttpClient4Util       Apache封装好的CloseableHttpClient
 * @Description: http工具类   apache httpClient4.5
 * @date 2021/8/20
 */
public class HttpClient4Util {

    private static Logger logger= LoggerFactory.getLogger(HttpClient4Util.class);

    private static RequestConfig requestConfig;
    static {
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)  //连接超时时间
                .setConnectionRequestTimeout(5000)  //请求超时时间
                .setSocketTimeout(6000) //数据读取超时时间
                .build();
    }

    /**
     *
     * @param url          请求路径
     * @param paramMap    请求参数
     * @param headerMap   header 头部参数
     * @return String
     */
    public static String doGet(String url, Map<String, Object> paramMap, Map<String, String> headerMap) {

        String result = StringUtils.EMPTY;
        StringBuffer sbf = new StringBuffer(url);

        try {
            if (MapUtils.isNotEmpty(paramMap)) {
                sbf.append(sbf.indexOf("?") == -1 ? "?" : "&");
                for (String key : paramMap.keySet()) {
                    String value = Objects.isNull(paramMap.get(key)) ? StringUtils.EMPTY :
                            String.valueOf(paramMap.get(key));
                    sbf.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
                }
                sbf.deleteCharAt(sbf.length() - 1);
            }
            logger.info("发送 get 请求 路径 [{}]", sbf.toString());

            HttpGet httpGet = new HttpGet(sbf.toString());
            httpGet.setConfig(requestConfig);
            if (MapUtils.isNotEmpty(headerMap)) {
                for (String key : headerMap.keySet()) {
                    httpGet.setHeader(key, headerMap.get(key));//设置一个请求头字段，有则覆盖，无则添加
                }
            }
            HttpResponse response = HttpClients.createDefault().execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            logger.error("发送 get 请求 [{}] 发生异常 ", url, e);
        }
        return result;
    }

    /**
     * 发送 contentType为 application/json 请求
     * @param url             请求地址
     * @param paramObj       请求参数
     * @param headerMap      请求头
     * @return String
     */
    public static String doPostJson(String url, Object paramObj, Map<String, String> headerMap) {
        String result = StringUtils.EMPTY;

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            //添加自定义header 头
            if (MapUtils.isNotEmpty(headerMap)) {
                for (String key : headerMap.keySet()) {
                    httpPost.setHeader(key, headerMap.get(key));//设置一个请求头字段，有则覆盖，无则添加
                }
            }
            HttpEntity entity = new StringEntity(Objects.nonNull(paramObj) ? JSON.toJSONString(paramObj) : "",
                    ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            HttpResponse response = HttpClients.createDefault().execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            logger.error("发送 post 请求 [{}] 发生异常 ", url, e);
        }
        return result;
    }

    /**
     * 发送 contentType为 application/x-www-form-urlencoded 请求
     * @param url             请求地址
     * @param paramMap       请求参数
     * @param headerMap      请求头
     * @return String
     */
    public static String doPost(String url, Map<String, Object> paramMap, Map<String, String> headerMap) {
        String result = StringUtils.EMPTY;

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            //添加自定义header 头
            if (MapUtils.isNotEmpty(headerMap)) {
                for (String key : headerMap.keySet()) {
                    httpPost.setHeader(key, headerMap.get(key));//设置一个请求头字段，有则覆盖，无则添加
                }
            }
            //请求参数
            List<NameValuePair> list=new ArrayList<>();
            if (MapUtils.isNotEmpty(paramMap)) {
                for (String key : paramMap.keySet()) {
                   list.add(new BasicNameValuePair(key,String.valueOf(paramMap.get(key))));
                }
            }
            HttpEntity entity = new UrlEncodedFormEntity(list,Consts.UTF_8.name());
            httpPost.setEntity(entity);

            HttpResponse response = HttpClients.createDefault().execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            logger.error("发送 post 请求 [{}] 发生异常 ", url, e);
        }
        return result;
    }

    public static void main(String[] args) {
//        String url="http://www.gztest.pdmiryun.com/cpcapi/server/screen/show/list";
//        String url="https://napp.newgscloud.com/largescreen/init/common/execute";
        String url="http://www.gztest.pdmiryun.com/contentapi/server/bigscreen/common/getContentListByChannelCode";
        Map<String,Object> paramMap= new HashMap<>();
        paramMap.put("channelCode","sy");
        paramMap.put("siteId","23");

        Map<String, String> headerMap = new HashMap<>();
//        headerMap.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

        String result = doGet(url, paramMap, headerMap);
        System.out.println("get result :" + result);

        result = doPostJson(url, paramMap, headerMap);
        System.out.println("postJson result :" + result);

        result = doPost(url, paramMap, headerMap);
        System.out.println("post result :" + result);
    }

}
