package com.chang.until;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chang.common.CommUtils;
import com.chang.common.exception.Response;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class HttpUtil {
   public static Logger log = Logger.getLogger(HttpUtil.class);

   public static String getHttpRequest(String server, String param, String method) throws IOException {
      String strData = null;
      HttpURLConnection conn = null;
      OutputStream outputStream = null;
      InputStream inputStream = null;
      ByteArrayOutputStream baos = null;
      byte[] buffer = new byte[512];

      try {
         URL url = new URL(server);
         conn = (HttpURLConnection)url.openConnection();
         conn.setDoInput(true);
         conn.setDoOutput(true);
         conn.setRequestMethod(method);
         conn.setRequestProperty("Content-Type", "application/octet-stream");
         outputStream = conn.getOutputStream();
         outputStream.write(param.getBytes("UTF-8"));
         outputStream.flush();
         inputStream = conn.getInputStream();
         baos = new ByteArrayOutputStream();

         int len;
         while((len = inputStream.read(buffer, 0, 512)) != -1) {
            baos.write(buffer, 0, len);
         }

         strData = new String(baos.toByteArray(), "UTF-8");
         return strData;
      } catch (IOException var14) {
         throw var14;
      } finally {
         if (inputStream != null) {
            inputStream.close();
            inputStream = null;
         }

         if (outputStream != null) {
            outputStream.close();
            outputStream = null;
         }

         if (baos != null) {
            baos.close();
            baos = null;
         }

         if (conn != null) {
            conn.disconnect();
         }

      }
   }

   public static String getHttpRequest(String server, byte[] bytes, String method) throws IOException {
      String strData = null;
      HttpURLConnection conn = null;
      OutputStream outputStream = null;
      InputStream inputStream = null;
      ByteArrayOutputStream baos = null;
      byte[] buffer = new byte[512];

      try {
         URL url = new URL(server);
         conn = (HttpURLConnection)url.openConnection();
         conn.setDoInput(true);
         conn.setDoOutput(true);
         conn.setRequestMethod(method);
         conn.setRequestProperty("Content-Type", "application/octet-stream");
         outputStream = conn.getOutputStream();
         outputStream.write(bytes);
         outputStream.flush();
         inputStream = conn.getInputStream();
         baos = new ByteArrayOutputStream();

         int len;
         while((len = inputStream.read(buffer, 0, 512)) != -1) {
            baos.write(buffer, 0, len);
         }

         strData = new String(baos.toByteArray(), "UTF-8");
         return strData;
      } catch (IOException var14) {
         throw var14;
      } finally {
         if (inputStream != null) {
            inputStream.close();
            inputStream = null;
         }

         if (outputStream != null) {
            outputStream.close();
            outputStream = null;
         }

         if (baos != null) {
            baos.close();
            baos = null;
         }

         if (conn != null) {
            conn.disconnect();
         }

      }
   }

   public static String sendGet(HttpServletRequest request, String url, String param) {
      String result = "";
      BufferedReader in = null;

      try {
         String urlNameString = url + "?" + param;
         URL realUrl = new URL(urlNameString);
         URLConnection connection = realUrl.openConnection();
         connection.setRequestProperty("accept", "*/*");
         connection.setRequestProperty("connection", "Keep-Alive");
         connection.addRequestProperty("Cookie", getSessionIncookie(request, url));
         connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
         String token = (String)request.getSession().getAttribute("token");
         if (null != token) {
            connection.setRequestProperty("token", token);
         }

         String name = (String)request.getSession().getAttribute("name");
         if (null != name) {
            connection.setRequestProperty("name", name);
         }

         String TenantId = (String)request.getSession().getAttribute("Tenant-Id");
         if (null != TenantId) {
            connection.setRequestProperty("Tenant-Id", TenantId);
         }

         String projectName = (String)request.getSession().getAttribute("projectName");
         if (null != token) {
            connection.setRequestProperty("projectName", projectName);
         }

         connection.connect();
         getCookieToSession(request, connection);
         Map<String, List<String>> map = connection.getHeaderFields();
         Iterator var13 = map.keySet().iterator();

         String tokenEx;
         while(var13.hasNext()) {
            tokenEx = (String)var13.next();
            log.info(tokenEx + "--->" + map.get(tokenEx));
         }

         String line;
         for(in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8")); (line = in.readLine()) != null; result = result + line) {
         }

         tokenEx = null;
         if (StringUtils.isNotEmpty(result)) {
            JSONObject re = JSONObject.parseObject(result);
            if (null != re && re.containsKey("data")) {
               JSONObject dataEx = re.getJSONObject("data");
               if (null != dataEx && dataEx.containsKey("token")) {
                  tokenEx = dataEx.getString("token");
               }
            }
         }

         if (null != tokenEx) {
            request.getSession().setAttribute("token", tokenEx);
         }
      } catch (IOException var27) {
         log.error("sendGet error IOException", var27);
      } catch (Exception var28) {
         log.error("sendGet error", var28);
      } finally {
         try {
            if (in != null) {
               in.close();
            }
         } catch (Exception var26) {
         }

      }

      return result;
   }

   public static String sendPost(HttpServletRequest request, String url, String param) throws IOException, Exception {
      PrintWriter out = null;
      BufferedReader in = null;
      String result = "";

      try {
         URL realUrl = new URL(url);
         URLConnection conn = realUrl.openConnection();
         conn.setRequestProperty("accept", "*/*");
         conn.setRequestProperty("connection", "Keep-Alive");
         conn.addRequestProperty("Cookie", getSessionIncookie(request, url));
         conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
         conn.setDoOutput(true);
         conn.setDoInput(true);
         out = new PrintWriter(conn.getOutputStream());
         out.print(param);
         out.flush();

         String line;
         for(in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8")); (line = in.readLine()) != null; result = result + line) {
         }
      } catch (IOException var17) {
         throw var17;
      } catch (Exception var18) {
         throw var18;
      } finally {
         try {
            if (out != null) {
               out.close();
            }

            if (in != null) {
               in.close();
            }
         } catch (IOException var16) {
         }

      }

      return result;
   }

   public static String sendPostAndPutAndDeleteJson(HttpServletRequest request, JSONObject jsonParam, String urls, String urlParam, String method) {
      StringBuffer sb = new StringBuffer();

      try {
         String realUrl = urls;
         if (!StringUtils.isBlank(urlParam)) {
            if (urlParam.indexOf("?") == 0) {
               realUrl = urls + urlParam;
            } else {
               realUrl = urls + "?" + urlParam;
            }
         }

         if (jsonParam == null) {
            jsonParam = new JSONObject();
         }

         URL url = new URL(realUrl);
         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
         conn.setDoOutput(true);
         conn.setDoInput(true);
         conn.setUseCaches(false);
         conn.setRequestMethod(method);
         conn.setRequestProperty("Connection", "Keep-Alive");
         conn.setRequestProperty("Charset", "UTF-8");
         byte[] data = jsonParam.toString().getBytes();
         conn.setRequestProperty("Content-Length", String.valueOf(data.length));
         conn.setRequestProperty("Content-Type", "application/json");
         conn.addRequestProperty("Cookie", getSessionIncookie(request, urls));
         String token = (String)request.getSession().getAttribute("token");
         if (null != token) {
            conn.setRequestProperty("token", token);
         }

         String name = (String)request.getSession().getAttribute("name");
         if (null != name) {
            conn.setRequestProperty("name", name);
         }

         String TenantId = (String)request.getSession().getAttribute("Tenant-Id");
         if (null != TenantId) {
            conn.setRequestProperty("Tenant-Id", TenantId);
         }

         String projectName = (String)request.getSession().getAttribute("projectName");
         if (null != token) {
            conn.setRequestProperty("projectName", projectName);
         }

         conn.connect();
         OutputStream out = new DataOutputStream(conn.getOutputStream());
         out.write(jsonParam.toString().getBytes());
         out.flush();
         getCookieToSession(request, conn);
         out.close();
         if (200 == conn.getResponseCode()) {
            InputStream in1 = conn.getInputStream();

            try {
               new String();
               BufferedReader responseReader = new BufferedReader(new InputStreamReader(in1, "UTF-8"));

               String readLine;
               while((readLine = responseReader.readLine()) != null) {
                  sb.append(readLine);
               }

               responseReader.close();
               String RequestReturnData = sb.toString();
               String tokenEx = null;
               if (StringUtils.isNotEmpty(RequestReturnData)) {
                  JSONObject re = JSONObject.parseObject(RequestReturnData);
                  if (null != re && re.containsKey("data")) {
                     JSONObject dataEx = re.getJSONObject("data");
                     if (null != dataEx && dataEx.containsKey("token")) {
                        tokenEx = dataEx.getString("token");
                     }
                  }
               }

               if (null != tokenEx) {
                  request.getSession().setAttribute("token", tokenEx);
               }
            } catch (Exception var22) {
               var22.printStackTrace();
            }
         } else {
            log.info("error code: " + conn.getResponseCode());
         }
      } catch (Exception var23) {
         log.error("sendPostJson error!", var23);
      }

      return sb.toString();
   }

   private static String getSessionIncookie(HttpServletRequest request, String url) {
      HttpSession session = request.getSession();
      String JSESSIONID = (String)session.getAttribute("loginSession");
      String domain = getDomain(url);
      System.out.println(JSESSIONID);
      return "SESSION=" + JSESSIONID + "; path=/; domain=" + domain + "; HttpOnly;";
   }

   private static String getDomain(String url) {
      String domain = "";
      url = url.replace("http://", "");
      if (url.contains(":")) {
         domain = url.split(":")[0];
      } else {
         domain = url.split("/")[0];
      }

      return domain;
   }

   private static void getCookieToSession(HttpServletRequest request, URLConnection connection) {
      Map<String, List<String>> map = connection.getHeaderFields();
      HttpSession session = request.getSession();
      List<String> cookie = (List)map.get("Set-Cookie");
      if (cookie != null) {
         String JSESSIONID = getCookieBySet("SESSION", (String)cookie.get(0));
         if (JSESSIONID != null) {
            System.out.println(JSESSIONID);
            session.setAttribute("loginSession", JSESSIONID);
         }
      }

   }

   public static String getCookieBySet(String name, String set) {
      String regex = name + "=(.*?);";
      String result = "";
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(set);
      if (matcher.find()) {
         result = matcher.group();
         result = result.split("=")[1];
         result = result.replace(";", "");
         return result;
      } else {
         return null;
      }
   }

   public static <T> List<T> getData(String body, Class<T> tClass) {
      Response response = (Response)CommUtils.getObjectFromJSONString(body, Response.class);
      if (response.isSuccess()) {
         List<T> list = new ArrayList();
         if (response.getData().isPresent()) {
            Object o = response.getData().get();
            if (o instanceof JSONArray) {
               JSONArray jaTemp = (JSONArray)o;
               jaTemp.forEach((x) -> {
                  if (x instanceof JSONObject) {
                     JSONObject jTemp = (JSONObject)x;
                     T tBean = CommUtils.getObjectFromJSONString(jTemp.toJSONString(), tClass);
                     list.add(tBean);
                  }

               });
            } else if (o instanceof JSONObject) {
               JSONObject jTemp = (JSONObject)o;
               T tBean = CommUtils.getObjectFromJSONString(jTemp.toJSONString(), tClass);
               list.add(tBean);
            } else {
               if (!(o instanceof String)) {
                  return null;
               }

               String sTemp = (String)o;
               list.add((T) sTemp);
            }
         }

         return list;
      } else {
         throw new RuntimeException(response.getErrMessage());
      }
   }
}
