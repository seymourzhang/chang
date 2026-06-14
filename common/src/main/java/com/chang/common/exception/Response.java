package com.chang.common.exception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.chang.uclass.bytecode.ClassUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Response<T> implements Serializable {
   private static final long serialVersionUID = -1897069917179903275L;
   private boolean isSuccess;
   private String errCode;
   private String errMessage;
   private Optional<?> data;
   @JSONField(
      serialize = false
   )
   private IErrorCode iErrorCode;

   public static <T extends Serializable> Response<T> getInstance(Class<T> clazz) {
      return new Response();
   }

   public static Response getInstance() {
      return new Response();
   }

   public Response<T> buildFailure(IErrorCode iErrorCode) {
      this.setSuccess(false);
      this.setErrCode(iErrorCode.getErrCode());
      this.setErrMessage(iErrorCode.getErrDesc());
      this.setData(Optional.empty());
      return this;
   }

   public Response<T> buildSuccess() {
      this.setSuccess(true);
      this.setData(Optional.empty());
      return this;
   }

   public Response<T> buildFailure(IErrorCode iErrorCode, T data) {
      this.setSuccess(false);
      this.setErrCode(iErrorCode.getErrCode());
      this.setErrMessage(iErrorCode.getErrDesc());
      this.setData(Optional.ofNullable(data));
      return this;
   }

   public Response<T> buildFailure(IErrorCode iErrorCode, List<T> listData) {
      this.setSuccess(false);
      this.setErrCode(iErrorCode.getErrCode());
      this.setErrMessage(iErrorCode.getErrDesc());
      this.setData(Optional.ofNullable(listData));
      return this;
   }

   public Response<T> buildFailure(String errCode, String errMessage, T data) {
      this.setSuccess(false);
      this.setErrCode(errCode);
      this.setErrMessage(errMessage);
      this.setData(Optional.ofNullable(data));
      return this;
   }

   public Response<T> buildFailure(String errCode, String errMessage) {
      this.setSuccess(false);
      this.setErrCode(errCode);
      this.setErrMessage(errMessage);
      return this;
   }

   public Response<T> buildFailure() {
      this.setSuccess(false);
      return this;
   }

   public Response<T> buildFailure(String errCode, String errMessage, List<T> listData) {
      this.setSuccess(false);
      this.setErrCode(errCode);
      this.setErrMessage(errMessage);
      this.setData(Optional.ofNullable(listData));
      return this;
   }

   public Response<T> buildSuccess(T data) {
      this.setSuccess(true);
      this.setData(Optional.ofNullable(data));
      return this;
   }

   public Response<T> buildSuccess(List<T> listData) {
      this.setSuccess(true);
      this.setData(Optional.ofNullable(listData));
      return this;
   }

   public Response<T> buildSuccess(PageInfo<T> pageDate) {
      this.setSuccess(true);
      this.setData(Optional.ofNullable(pageDate));
      return this;
   }

   public Response<T> buildSuccess(Page<T> pageDate) {
      this.setSuccess(true);
      this.setData(Optional.ofNullable(pageDate));
      return this;
   }

   public static <R> List<R> getResponseData(Response response, Class<R> clazz) {
      if (response.getData().isPresent()) {
         Object o = response.getData().get();
         JSONArray jsonArray = null;
         if (o instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject)o;
            jsonArray = new JSONArray();
            jsonArray.add(jsonObject);
         } else if (o instanceof JSONArray) {
            jsonArray = (JSONArray)o;
         } else {
            if (!ClassUtils.isSimpleType(o.getClass())) {
               return null;
            }

            jsonArray = new JSONArray();
            jsonArray.add(o);
         }

         return JSONArray.parseArray(jsonArray.toString(), clazz);
      } else {
         return null;
      }
   }

   public boolean isSuccess() {
      return this.isSuccess;
   }

   public String getErrCode() {
      return this.errCode;
   }

   public String getErrMessage() {
      return this.errMessage;
   }

   public Optional<?> getData() {
      return this.data;
   }

   public IErrorCode getIErrorCode() {
      return this.iErrorCode;
   }

   public void setSuccess(boolean isSuccess) {
      this.isSuccess = isSuccess;
   }

   public void setErrCode(String errCode) {
      this.errCode = errCode;
   }

   public void setErrMessage(String errMessage) {
      this.errMessage = errMessage;
   }

   public void setData(Optional<?> data) {
      this.data = data;
   }

   public void setIErrorCode(IErrorCode iErrorCode) {
      this.iErrorCode = iErrorCode;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Response)) {
         return false;
      } else {
         Response<?> other = (Response)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.isSuccess() != other.isSuccess()) {
            return false;
         } else {
            label61: {
               Object this$errCode = this.getErrCode();
               Object other$errCode = other.getErrCode();
               if (this$errCode == null) {
                  if (other$errCode == null) {
                     break label61;
                  }
               } else if (this$errCode.equals(other$errCode)) {
                  break label61;
               }

               return false;
            }

            label54: {
               Object this$errMessage = this.getErrMessage();
               Object other$errMessage = other.getErrMessage();
               if (this$errMessage == null) {
                  if (other$errMessage == null) {
                     break label54;
                  }
               } else if (this$errMessage.equals(other$errMessage)) {
                  break label54;
               }

               return false;
            }

            Object this$data = this.getData();
            Object other$data = other.getData();
            if (this$data == null) {
               if (other$data != null) {
                  return false;
               }
            } else if (!this$data.equals(other$data)) {
               return false;
            }

            Object this$iErrorCode = this.getIErrorCode();
            Object other$iErrorCode = other.getIErrorCode();
            if (this$iErrorCode == null) {
               if (other$iErrorCode != null) {
                  return false;
               }
            } else if (!this$iErrorCode.equals(other$iErrorCode)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof Response;
   }

   public int hashCode() {
      int result = 1;
      result = result * 59 + (this.isSuccess() ? 79 : 97);
      Object $errCode = this.getErrCode();
      result = result * 59 + ($errCode == null ? 43 : $errCode.hashCode());
      Object $errMessage = this.getErrMessage();
      result = result * 59 + ($errMessage == null ? 43 : $errMessage.hashCode());
      Object $data = this.getData();
      result = result * 59 + ($data == null ? 43 : $data.hashCode());
      Object $iErrorCode = this.getIErrorCode();
      result = result * 59 + ($iErrorCode == null ? 43 : $iErrorCode.hashCode());
      return result;
   }

   public String toString() {
      return "Response(isSuccess=" + this.isSuccess() + ", errCode=" + this.getErrCode() + ", errMessage=" + this.getErrMessage() + ", data=" + this.getData() + ", iErrorCode=" + this.getIErrorCode() + ")";
   }

   public Response() {
   }

   public Response(boolean isSuccess, String errCode, String errMessage, Optional<?> data, IErrorCode iErrorCode) {
      this.isSuccess = isSuccess;
      this.errCode = errCode;
      this.errMessage = errMessage;
      this.data = data;
      this.iErrorCode = iErrorCode;
   }
}
