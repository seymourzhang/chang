package com.chang.common;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

public class ResultUtils {
   public static JSONObject respOk() {
      JSONObject resp = new JSONObject();
      resp.put("rtn_code", "0");
      resp.put("rtn_msg", "OK");
      return resp;
   }

   public static JSONObject respErr() {
      JSONObject resp = new JSONObject();
      resp.put("rtn_code", "-1");
      resp.put("rtn_msg", "exception");
      return resp;
   }

   public static JSONObject respPageBean(int page, int totalPages, Long totalElements, int size, List list) {
      JSONObject pageBean = new JSONObject();
      pageBean.put("page", page);
      pageBean.put("size", size);
      pageBean.put("total", totalElements);
      pageBean.put("list", list);
      pageBean.put("totalPage", totalPages);
      pageBean.put("rtn_code", "0");
      pageBean.put("rtn_msg", "OK");
      return pageBean;
   }

   public static JSONObject getResultDes(String code, String msg, String des) {
      JSONObject resp = new JSONObject();
      resp.put("rtn_code", code);
      resp.put("rtn_msg", msg);
      resp.put("rtn_des", des);
      return resp;
   }

   public static JSONObject getResult(String rtnCode, String rtnMsg) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("rtn_code", rtnCode);
      jsonObject.put("rtn_msg", rtnMsg);
      return jsonObject;
   }

   public static JSONObject getResult(String rtnCode, String rtnMsg, Object obj) {
      JSONObject jsonObject = getResult(rtnCode, rtnMsg);
      jsonObject.put("data", obj);
      return jsonObject;
   }

   public static JSONObject getResult(String rtnCode, String rtnMsg, List list) {
      JSONObject jsonObject = getResult(rtnCode, rtnMsg);
      jsonObject.put("list", list);
      return jsonObject;
   }
}
