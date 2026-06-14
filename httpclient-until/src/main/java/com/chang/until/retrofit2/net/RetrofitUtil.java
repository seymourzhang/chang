package com.chang.until.retrofit2.net;

import com.chang.common.SslUtil;
import com.chang.until.retrofit2.converter.FastJsonConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitUtil {
   public static Retrofit getRetrofit(String baseUrl) {
      Retrofit retrofit = (new Retrofit.Builder()).baseUrl(baseUrl).addConverterFactory(FastJsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
      return retrofit;
   }

   public static Retrofit getRetrofitHttps(String baseUrl, String caCrtFile, String crtFile, String keyFile, String password) throws Exception {
      OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
      okhttpBuilder.socketFactory(SslUtil.getSocketFactory(caCrtFile, crtFile, keyFile, password));
      OkHttpClient client = okhttpBuilder.build();
      Retrofit retrofit = (new Retrofit.Builder()).baseUrl(baseUrl).addConverterFactory(FastJsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(client).build();
      return retrofit;
   }
}
