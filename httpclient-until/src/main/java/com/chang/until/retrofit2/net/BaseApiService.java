package com.chang.until.retrofit2.net;

import io.reactivex.Flowable;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface BaseApiService {
   @GET
   Flowable<Object> get(@Url String var1, @QueryMap Map<String, Object> var2);

   @FormUrlEncoded
   @POST
   Flowable<Object> post(@Url String var1, @FieldMap Map<String, Object> var2);

   @POST
   Flowable<Object> json(@Url String var1, @Body RequestBody var2);
}
