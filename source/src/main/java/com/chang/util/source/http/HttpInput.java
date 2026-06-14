package com.chang.util.source.http;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.chang.util.source.common.SourceContext;
import com.chang.until.retrofit2.net.BaseSubscriber;
import com.chang.until.retrofit2.net.RetrofitClient;
import com.chang.until.timeTaskApi.TimeTaskManage;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import io.reactivex.Flowable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInput implements InputSource<Object, Object> {
   private static final Logger log = LoggerFactory.getLogger(HttpInput.class);
   private String baseUrl;
   private String url;
   private String type;
   private Long time;
   private String keyName;
   private Map<String, Object> parm;
   private volatile Flowable flowable;
   private volatile Boolean first = false;
   private Map<String, Object> exParm;

   public HttpInput(String baseUrl, String url, String type, String keyName, Long time, Map<String, Object> parm, Map<String, Object> exParm) {
      this.baseUrl = baseUrl;
      this.url = url;
      this.type = type;
      this.parm = parm;
      this.time = time;
      this.keyName = keyName;
      this.exParm = exParm;
   }

   public void setUpParm(Map<String, Object> parm) {
      this.parm = parm;
      this.loadFlowAble();
   }

   private void loadFlowAble() {
      switch (this.type) {
         case "get":
            this.flowable = RetrofitClient.getInstance(this.baseUrl).createBaseApi().get(this.url, this.parm);
            break;
         case "post":
            this.flowable = RetrofitClient.getInstance(this.baseUrl).createBaseApi().post(this.url, this.parm);
            break;
         case "json":
            JSONObject json = new JSONObject(this.parm);
            RequestBody body = RequestBody.create(json.toJSONString(), MediaType.parse("application/json; charset=utf-8"));
            this.flowable = RetrofitClient.getInstance(this.baseUrl).createBaseApi().json(this.url, body);
            break;
         default:
            throw new RuntimeException("type Err");
      }

   }

   public void InPut(OutputSource source, Function<Object, Object> function) {
      this.loadFlowAble();
      Runnable runnable = () -> {
         this.flowable.subscribe(new BaseSubscriber<Object>() {
            public void onNext(Object responseBody) {
               SourceContext.setExParm(HttpInput.this.exParm);
               if (ObjectUtil.isNotNull(function)) {
                  source.Output(function.apply(responseBody.toString()));
               } else {
                  source.Output(responseBody.toString());
               }

               SourceContext.clearExParm();
            }

            public void onError(Throwable t) {
               HttpInput.log.error("Http Err:", t);
            }

            public void onComplete() {
               HttpInput.log.info("Http OK!");
            }
         });
      };
      if (!this.first) {
         TimeTaskManage.scheduleAtFixedRate(this.keyName, runnable, this.time, TimeUnit.MILLISECONDS);
         this.first = true;
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.exParm;
   }

   public void close() {
      TimeTaskManage.stopTask(this.keyName);
      TimeTaskManage.clearDoneTask();
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }
}
