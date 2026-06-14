package com.chang.until.retrofit2.net;

import com.alibaba.fastjson.JSONException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.conn.ConnectTimeoutException;
import retrofit2.HttpException;

public class ExceptionHandle {
   private static final int UNAUTHORIZED = 401;
   private static final int FORBIDDEN = 403;
   private static final int NOT_FOUND = 404;
   private static final int REQUEST_TIMEOUT = 408;
   private static final int INTERNAL_SERVER_ERROR = 500;
   private static final int BAD_GATEWAY = 502;
   private static final int SERVICE_UNAVAILABLE = 503;
   private static final int GATEWAY_TIMEOUT = 504;

   public static ResponeThrowable handleException(Throwable e) {
      ResponeThrowable ex;
      if (e instanceof HttpException) {
         HttpException httpException = (HttpException)e;
         ex = new ResponeThrowable(e, 1003);
         switch (httpException.code()) {
            case 401:
            case 403:
            case 404:
            case 408:
            case 500:
            case 502:
            case 503:
            case 504:
            default:
               ex.message = "网络错误";
               return ex;
         }
      } else if (e instanceof ServerException) {
         ServerException resultException = (ServerException)e;
         ex = new ResponeThrowable(resultException, resultException.code);
         ex.message = resultException.message;
         return ex;
      } else if (e instanceof JSONException) {
         ex = new ResponeThrowable(e, 1001);
         ex.message = "解析错误";
         return ex;
      } else if (e instanceof ConnectException) {
         ex = new ResponeThrowable(e, 1002);
         ex.message = "连接失败";
         return ex;
      } else if (e instanceof SSLHandshakeException) {
         ex = new ResponeThrowable(e, 1005);
         ex.message = "证书验证失败";
         return ex;
      } else if (e instanceof ConnectTimeoutException) {
         ex = new ResponeThrowable(e, 1006);
         ex.message = "连接超时";
         return ex;
      } else if (e instanceof SocketTimeoutException) {
         ex = new ResponeThrowable(e, 1006);
         ex.message = "连接超时";
         return ex;
      } else {
         ex = new ResponeThrowable(e, 1000);
         ex.message = "未知错误";
         return ex;
      }
   }

   public class ServerException extends RuntimeException {
      public int code;
      public String message;
   }

   class ERROR {
      public static final int UNKNOWN = 1000;
      public static final int PARSE_ERROR = 1001;
      public static final int NETWORD_ERROR = 1002;
      public static final int HTTP_ERROR = 1003;
      public static final int SSL_ERROR = 1005;
      public static final int TIMEOUT_ERROR = 1006;
   }

   public static class ResponeThrowable extends Exception {
      public int code;
      public String message;

      public ResponeThrowable(Throwable throwable, int code) {
         super(throwable);
         this.code = code;
      }
   }
}
