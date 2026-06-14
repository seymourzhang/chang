package com.chang.until.retrofit2.net;

public abstract class CallBack {
   public void onStart() {
   }

   public void onCompleted() {
   }

   public abstract void onError(Throwable var1);

   public void onProgress(long fileSizeDownloaded) {
   }

   public abstract void onSucess(String var1, String var2, long var3);
}
