package com.chang.util.source.http;

import com.alibaba.fastjson.JSONObject;
import com.chang.until.retrofit2.net.BaseSubscriber;
import com.chang.until.retrofit2.net.RetrofitClient;
import com.chang.util.source.OutputSource;
import io.reactivex.Flowable;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpOutput implements OutputSource {
   private static final Logger log = LoggerFactory.getLogger(HttpOutput.class);
   private String baseUrl;
   private String url;
   private String type;
   private String keyName;
   private Map<String, Object> parm;

   public HttpOutput(String keyName, String baseUrl, String url, String type, Map<String, Object> parm) {
      this.baseUrl = baseUrl;
      this.url = url;
      this.type = type;
      this.parm = parm;
   }

   public void Output(Object o) {
      if (!(o instanceof Map)) {
         throw new RuntimeException("parm is not map");
      } else {
         Flowable flowable;
         switch (this.type) {
            case "get":
               flowable = RetrofitClient.getInstance(this.baseUrl).createBaseApi().get(this.url, (Map)o);
               break;
            case "post":
               flowable = RetrofitClient.getInstance(this.baseUrl).createBaseApi().post(this.url, (Map)o);
               break;
            case "json":
               JSONObject json = new JSONObject((Map)o);
               RequestBody body = RequestBody.create(json.toJSONString(), MediaType.parse("application/json; charset=utf-8"));
               flowable = RetrofitClient.getInstance(this.baseUrl).createBaseApi().json(this.url, body);
               break;
            default:
               throw new RuntimeException("type Err");
         }

         flowable.subscribe(new BaseSubscriber<Object>() {
            public void onNext(Object responseBody) {
               HttpOutput.log.info(responseBody.toString());
            }

            public void onError(Throwable t) {
               HttpOutput.log.error("Http Err:", t);
            }

            public void onComplete() {
               HttpOutput.log.info("Http OK!");
            }
         });
      }
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
   }
}
