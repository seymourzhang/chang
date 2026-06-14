package com.chang.until.retrofit2.net;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class BaseInterceptor implements Interceptor {
   private Map<String, String> headers;

   public BaseInterceptor(Map<String, String> headers) {
      this.headers = headers;
   }

   @NotNull
   public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
      Request.Builder builder = chain.request().newBuilder();
      if (this.headers != null && this.headers.size() > 0) {
         Set<String> keys = this.headers.keySet();
         Iterator var4 = keys.iterator();

         while(var4.hasNext()) {
            String headerKey = (String)var4.next();
            builder.addHeader(headerKey, (String)this.headers.get(headerKey)).build();
         }
      }

      return chain.proceed(builder.build());
   }
}
