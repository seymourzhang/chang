package com.chang.until.retrofit2.net;

import com.chang.until.retrofit2.converter.FastJsonConverterFactory;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.apache.http.util.TextUtils;
import org.reactivestreams.Publisher;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitClient {
   private static final int DEFAULT_TIMEOUT = 20;
   private final Retrofit retrofit;
   private BaseApiService apiService;

   private RetrofitClient(String url) {
      if (TextUtils.isEmpty(url)) {
         throw new RuntimeException("url is not null");
      } else {
         OkHttpClient okHttpClient = (new OkHttpClient.Builder()).addNetworkInterceptor((new HttpLoggingInterceptor()).setLevel(Level.BODY)).connectTimeout(20L, TimeUnit.SECONDS).writeTimeout(20L, TimeUnit.SECONDS).readTimeout(20L, TimeUnit.SECONDS).connectionPool(new ConnectionPool(8, 15L, TimeUnit.SECONDS)).build();
         this.retrofit = (new Retrofit.Builder()).client(okHttpClient).addConverterFactory(FastJsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(url).build();
      }
   }

   private RetrofitClient(String url, Map<String, String> headers) {
      if (TextUtils.isEmpty(url)) {
         throw new RuntimeException("url is not null");
      } else {
         OkHttpClient okHttpClient = (new OkHttpClient.Builder()).addInterceptor(new BaseInterceptor(headers)).addNetworkInterceptor((new HttpLoggingInterceptor()).setLevel(Level.BODY)).connectTimeout(20L, TimeUnit.SECONDS).writeTimeout(20L, TimeUnit.SECONDS).readTimeout(20L, TimeUnit.SECONDS).connectionPool(new ConnectionPool(8, 15L, TimeUnit.SECONDS)).build();
         this.retrofit = (new Retrofit.Builder()).client(okHttpClient).addConverterFactory(FastJsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(url).build();
      }
   }

   public static RetrofitClient getInstance(String url) {
      return new RetrofitClient(url);
   }

   public RetrofitClient createBaseApi() {
      this.apiService = (BaseApiService)this.create(BaseApiService.class);
      return this;
   }

   public <T> T create(Class<T> service) {
      if (service == null) {
         throw new RuntimeException("Api service is null!");
      } else {
         return this.retrofit.create(service);
      }
   }

   public Flowable get(String url, Map parameters) {
      return this.apiService.get(url, parameters).compose(this.schedulersTransformer()).compose(this.transformer());
   }

   public Flowable post(String url, Map<String, Object> parameters) {
      return this.apiService.post(url, parameters).compose(this.schedulersTransformer()).compose(this.transformer());
   }

   public Flowable json(String url, RequestBody jsonStr) {
      return this.apiService.json(url, jsonStr).compose(this.schedulersTransformer()).compose(this.transformer());
   }

   FlowableTransformer schedulersTransformer() {
      return new FlowableTransformer() {
         public Publisher apply(@NonNull Flowable upstream) {
            return upstream.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(Schedulers.io());
         }
      };
   }

   public FlowableTransformer transformer() {
      return (upstream) -> {
         return upstream.onErrorResumeNext(new HttpResponseFunc());
      };
   }

   private static class HttpResponseFunc implements Function<Throwable, Flowable> {
      private HttpResponseFunc() {
      }

      public Flowable apply(@NonNull Throwable t) throws Exception {
         return Flowable.error(ExceptionHandle.handleException(t));
      }

      // $FF: synthetic method
      HttpResponseFunc(Object x0) {
         this();
      }
   }
}
