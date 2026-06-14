package com.chang.util.source.spring.start;

public class Utils {
   public static String getSqliteUrlPath(String url) {
      String tempUrl = url.replace("jdbc:sqlite:", "");
      int end = tempUrl.lastIndexOf("/");
      return tempUrl.substring(0, end);
   }
}
