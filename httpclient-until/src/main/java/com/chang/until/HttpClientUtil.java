package com.chang.until;

import com.alibaba.fastjson.JSONObject;
import com.chang.common.logger.Logger;
import com.chang.common.logger.LoggerFactory;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
   private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
   private String JSESSIONID = null;
   private String TOKEN = null;
   public static final String METHOD_DELETE = "delete";
   public static final String METHOD_GET = "get";
   public static final String METHOD_POST = "post";
   public static final String METHOD_PUT = "put";

   private void getJsessionId(HttpResponse httpResponse) {
      if (null != httpResponse) {
         Header header = httpResponse.getFirstHeader("Set-Cookie");
         if (null != header) {
            String setCookie = header.getValue();
            if (StringUtils.isNotEmpty(setCookie)) {
               this.JSESSIONID = setCookie.substring("session-id=".length());
            }
         }
      }

   }

   public JSONObject doPost(String postUrl, String jsonObject, Map<String, String> params) throws Exception {
      String retStr = "";
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
      CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
      HttpPost httpPost = new HttpPost(postUrl);
      RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
      httpPost.setConfig(requestConfig);
      httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
      if (null != jsonObject) {
         StringEntity stringEntity = new StringEntity(jsonObject, "UTF-8");
         stringEntity.setContentEncoding("UTF-8");
         httpPost.setEntity(stringEntity);
      }

      if (null != params) {
         Iterator var15 = params.entrySet().iterator();

         while(var15.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var15.next();
            httpPost.addHeader((String)entry.getKey(), (String)entry.getValue());
         }
      }

      CloseableHttpResponse response;
      if (null != this.JSESSIONID) {
         if (null != this.TOKEN) {
            httpPost.addHeader("token", this.TOKEN);
         }

         httpPost.addHeader("Cookie", "session-id=" + this.JSESSIONID);
         response = closeableHttpClient.execute(httpPost);
      } else {
         response = closeableHttpClient.execute(httpPost);
         this.getJsessionId(response);
      }

      HttpEntity httpEntity = response.getEntity();
      retStr = EntityUtils.toString(httpEntity, "UTF-8");
      logger.info("retStr: %s", new Object[]{retStr});
      if (StringUtils.isNotEmpty(retStr)) {
         JSONObject re = JSONObject.parseObject(retStr);
         if (null != re && re.containsKey("data")) {
            JSONObject dataEx = null;

            try {
               dataEx = re.getJSONObject("data");
            } catch (Exception var14) {
               dataEx = null;
            }

            if (null != dataEx && dataEx.containsKey("token")) {
               this.TOKEN = dataEx.getString("token");
            }
         }
      }

      closeableHttpClient.close();
      return JSONObject.parseObject(retStr);
   }

   public JSONObject doGet(String getUrl, Map<String, String> params) throws Exception {
      String retStr = "";
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
      CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
      HttpGet httpGet = new HttpGet(getUrl);
      RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
      httpGet.setConfig(requestConfig);
      if (null != params) {
         Iterator var8 = params.entrySet().iterator();

         while(var8.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var8.next();
            httpGet.addHeader((String)entry.getKey(), (String)entry.getValue());
         }
      }

      if (null != this.JSESSIONID) {
         httpGet.setHeader("Cookie", "session-id=" + this.JSESSIONID);
      }

      if (null != this.TOKEN) {
         httpGet.setHeader("token", this.TOKEN);
      }

      CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
      this.getJsessionId(response);
      HttpEntity httpEntity = response.getEntity();
      retStr = EntityUtils.toString(httpEntity, "UTF-8");
      closeableHttpClient.close();
      logger.info("retStr: %s", new Object[]{retStr});
      if (StringUtils.isNotEmpty(retStr)) {
         JSONObject re = JSONObject.parseObject(retStr);
         if (null != re) {
            Object data = re.get("data");
            if (data instanceof JSONObject && re.containsKey("data")) {
               JSONObject dataEx = re.getJSONObject("data");
               if (null != dataEx && dataEx.containsKey("token")) {
                  this.TOKEN = dataEx.getString("token");
               }
            }
         }
      }

      return JSONObject.parseObject(retStr);
   }

   public JSONObject doPut(String putUrl, String jsonObject, Map<String, String> params) throws Exception {
      String retStr = "";
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
      CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
      HttpPut httpPut = new HttpPut(putUrl);
      RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
      httpPut.setConfig(requestConfig);
      httpPut.setHeader("Content-Type", "application/json;charset=utf-8");
      if (null != jsonObject) {
         StringEntity stringEntity = new StringEntity(jsonObject, "UTF-8");
         stringEntity.setContentEncoding("UTF-8");
         httpPut.setEntity(stringEntity);
      }

      if (null != params) {
         Iterator var14 = params.entrySet().iterator();

         while(var14.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var14.next();
            httpPut.addHeader((String)entry.getKey(), (String)entry.getValue());
         }
      }

      if (null != this.JSESSIONID) {
         httpPut.addHeader("Cookie", "session-id=" + this.JSESSIONID);
      }

      if (null != this.TOKEN) {
         httpPut.setHeader("token", this.TOKEN);
      }

      CloseableHttpResponse response = closeableHttpClient.execute(httpPut);
      this.getJsessionId(response);
      HttpEntity httpEntity = response.getEntity();
      retStr = EntityUtils.toString(httpEntity, "UTF-8");
      closeableHttpClient.close();
      logger.info("retStr: %s", new Object[]{retStr});
      if (StringUtils.isNotEmpty(retStr)) {
         JSONObject re = JSONObject.parseObject(retStr);
         if (null != re) {
            Object data = re.get("data");
            if (data instanceof JSONObject && re.containsKey("data")) {
               JSONObject dataEx = re.getJSONObject("data");
               if (null != dataEx && dataEx.containsKey("token")) {
                  this.TOKEN = dataEx.getString("token");
               }
            }
         }
      }

      return JSONObject.parseObject(retStr);
   }

   public JSONObject doDelete(String deleteUrl, String jsonObject, Map<String, String> params) throws Exception {
      String retStr = "";
      HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
      CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

      class httpDeleteWithBody extends HttpEntityEnclosingRequestBase {
         public static final String METHOD_NAME = "DELETE";

         public httpDeleteWithBody() {
         }

         public httpDeleteWithBody(URI uri) {
            this.setURI(uri);
         }

         public httpDeleteWithBody(String uri) {
            this.setURI(URI.create(uri));
         }

         public String getMethod() {
            return "DELETE";
         }
      }

      httpDeleteWithBody httpDelete = new httpDeleteWithBody(deleteUrl);
      RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
      httpDelete.setConfig(requestConfig);
      httpDelete.setHeader("Content-Type", "application/json;charset=utf-8");
      if (null != jsonObject) {
         StringEntity stringEntity = new StringEntity(jsonObject, "UTF-8");
         stringEntity.setContentEncoding("UTF-8");
         httpDelete.setEntity(stringEntity);
      }

      if (null != params) {
         Iterator var14 = params.entrySet().iterator();

         while(var14.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var14.next();
            httpDelete.addHeader((String)entry.getKey(), (String)entry.getValue());
         }
      }

      if (null != this.JSESSIONID) {
         httpDelete.addHeader("Cookie", "session-id=" + this.JSESSIONID);
      }

      if (null != this.TOKEN) {
         httpDelete.setHeader("token", this.TOKEN);
      }

      CloseableHttpResponse response = closeableHttpClient.execute(httpDelete);
      this.getJsessionId(response);
      HttpEntity httpEntity = response.getEntity();
      retStr = EntityUtils.toString(httpEntity, "UTF-8");
      closeableHttpClient.close();
      logger.info("retStr: %s", new Object[]{retStr});
      if (StringUtils.isNotEmpty(retStr)) {
         JSONObject re = JSONObject.parseObject(retStr);
         if (null != re && re.containsKey("data")) {
            Object data = re.get("data");
            if (data instanceof JSONObject) {
               JSONObject dataEx = re.getJSONObject("data");
               if (null != dataEx && dataEx.containsKey("token")) {
                  this.TOKEN = dataEx.getString("token");
               }
            }
         }
      }

      return JSONObject.parseObject(retStr);
   }
}
