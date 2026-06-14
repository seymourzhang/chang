package com.chang.until.httpFileDownLoad;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.chang.common.CommUtils;
import com.chang.common.exception.Response;
import com.chang.common.logger.Logger;
import com.chang.common.logger.LoggerFactory;
import java.io.IOException;

public class FileFetch {
   private static final Logger logger = LoggerFactory.getLogger(FileFetch.class);
   private String sURL;
   private long nStartPos;
   private long nEndPos;
   private String cookie = null;
   private FileAccess fileAccess;

   public FileFetch(String sURL, String sName, long nStart, long nEnd) throws IOException {
      this.sURL = sURL;
      this.nStartPos = nStart;
      this.nEndPos = nEnd;
      this.fileAccess = new FileAccess(sName, this.nStartPos);
   }

   public void fileDownLoad() {
      HttpResponse response = null;

      try {
         String sProperty = "bytes=" + this.nStartPos + "-";
         JSONObject body = new JSONObject();
         body.put("Range", sProperty);
         response = HttpUtil.createPost(this.sURL).body(body.toJSONString()).timeout(-1).executeAsync();
         if (!response.isOk()) {
            logger.error("FileFetch::fileSiteDownLoad ResponseCode is not 200: " + this.sURL + " back:" + response.getStatus());
            throw new RuntimeException("ResponseCode is not 200");
         }

         Response res = (Response)CommUtils.deserialize(response.bodyBytes(), Response.class);
         if (!res.isSuccess()) {
            throw new RuntimeException(res.getErrMessage());
         }

         byte[] bytes = (byte[])((byte[])res.getData().get());
         if (bytes.length == 0) {
            throw new RuntimeException("没有下载到文件数据，请重试");
         }

         this.fileAccess.write(bytes, 0, bytes.length);
      } catch (Exception var13) {
         logger.error("FileFetch::fileSiteDownLoad Err", var13);
         throw new RuntimeException(var13);
      } finally {
         try {
            this.fileAccess.close();
            response.close();
         } catch (Exception var12) {
         }

      }

   }

   public String getSURL() {
      return this.sURL;
   }

   public long getNStartPos() {
      return this.nStartPos;
   }

   public long getNEndPos() {
      return this.nEndPos;
   }

   public String getCookie() {
      return this.cookie;
   }

   public FileAccess getFileAccess() {
      return this.fileAccess;
   }

   public void setSURL(String sURL) {
      this.sURL = sURL;
   }

   public void setNStartPos(long nStartPos) {
      this.nStartPos = nStartPos;
   }

   public void setNEndPos(long nEndPos) {
      this.nEndPos = nEndPos;
   }

   public void setCookie(String cookie) {
      this.cookie = cookie;
   }

   public void setFileAccess(FileAccess fileAccess) {
      this.fileAccess = fileAccess;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof FileFetch)) {
         return false;
      } else {
         FileFetch other = (FileFetch)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getNStartPos() != other.getNStartPos()) {
            return false;
         } else if (this.getNEndPos() != other.getNEndPos()) {
            return false;
         } else {
            label52: {
               Object this$sURL = this.getSURL();
               Object other$sURL = other.getSURL();
               if (this$sURL == null) {
                  if (other$sURL == null) {
                     break label52;
                  }
               } else if (this$sURL.equals(other$sURL)) {
                  break label52;
               }

               return false;
            }

            Object this$cookie = this.getCookie();
            Object other$cookie = other.getCookie();
            if (this$cookie == null) {
               if (other$cookie != null) {
                  return false;
               }
            } else if (!this$cookie.equals(other$cookie)) {
               return false;
            }

            Object this$fileAccess = this.getFileAccess();
            Object other$fileAccess = other.getFileAccess();
            if (this$fileAccess == null) {
               if (other$fileAccess != null) {
                  return false;
               }
            } else if (!this$fileAccess.equals(other$fileAccess)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof FileFetch;
   }

   public int hashCode() {
      int result = 1;
      long $nStartPos = this.getNStartPos();
      result = result * 59 + (int)($nStartPos >>> 32 ^ $nStartPos);
      long $nEndPos = this.getNEndPos();
      result = result * 59 + (int)($nEndPos >>> 32 ^ $nEndPos);
      Object $sURL = this.getSURL();
      result = result * 59 + ($sURL == null ? 43 : $sURL.hashCode());
      Object $cookie = this.getCookie();
      result = result * 59 + ($cookie == null ? 43 : $cookie.hashCode());
      Object $fileAccess = this.getFileAccess();
      result = result * 59 + ($fileAccess == null ? 43 : $fileAccess.hashCode());
      return result;
   }

   public String toString() {
      return "FileFetch(sURL=" + this.getSURL() + ", nStartPos=" + this.getNStartPos() + ", nEndPos=" + this.getNEndPos() + ", cookie=" + this.getCookie() + ", fileAccess=" + this.getFileAccess() + ")";
   }
}
