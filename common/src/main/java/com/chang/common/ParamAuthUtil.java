package com.chang.common;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamAuthUtil {
   public static List<String> authNotContainsKey(String[] params, JSONObject paramObject) {
      List<String> keys = new ArrayList();
      if (null != params && null != paramObject && params.length >= 1 && paramObject.size() >= 1) {
         String[] var3 = params;
         int var4 = params.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String paramKey = var3[var5];
            if (paramObject.containsKey(paramKey) && null != paramObject.get(paramKey)) {
               System.out.println(paramKey + ":" + paramObject.get(paramKey));
            } else {
               keys.add(paramKey);
            }
         }

         return keys;
      } else {
         return null;
      }
   }

   public static JSONObject CheckParms(String[] params, JSONObject paramObject) {
      JSONObject result = new JSONObject();
      List<String> checkResult = authNotContainsKey(params, paramObject);
      if (null == checkResult) {
         result.put("rtn_code", "1002");
         result.put("rtn_msg", "param is illegal");
         result.put("rtn_des", "String[] size is 0 or JSONObject size is 0");
         return result;
      } else if (checkResult.size() > 0) {
         result.put("rtn_code", "1002");
         result.put("rtn_msg", "param is illegal");
         result.put("rtn_des", checkResult.toString());
         return result;
      } else {
         return null;
      }
   }

   public static boolean isPhone(String phone) {
      String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
      if (phone.length() != 11) {
         System.out.println("手机号应为11位数");
         return false;
      } else {
         Pattern p = Pattern.compile(regex);
         Matcher m = p.matcher(phone);
         boolean isMatch = m.matches();
         if (!isMatch) {
            System.out.println("请填入正确的手机号");
         }

         return isMatch;
      }
   }

   public static boolean isPhoneNumber(String phone) {
      if (phone.length() != 11) {
         System.out.println("手机号应为11位数");
         return false;
      } else {
         Pattern pattern = Pattern.compile("[0-9]*");
         boolean isNum = pattern.matcher(phone).matches();
         if (!isNum) {
            System.out.println("必须是纯数字");
            return false;
         } else if (!phone.startsWith("1")) {
            System.out.println("必须是1开头");
            return false;
         } else {
            return true;
         }
      }
   }
}
