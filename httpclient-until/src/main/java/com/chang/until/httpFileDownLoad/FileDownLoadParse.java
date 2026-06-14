package com.chang.until.httpFileDownLoad;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

public class FileDownLoadParse {
   public static DownLoadFileInfo parse(JSONObject body, long fileSize) {
      String rangeHeader = body.getString("Range");
      if (!StringUtils.isEmpty(rangeHeader)) {
         String[] range = rangeHeader.replaceAll("bytes=", "").split("-");
         if (range.length == 0) {
            throw new RuntimeException("range.length == 0");
         } else {
            Long start = Long.parseLong(range[0]);
            Long download_bytes = fileSize - start + 1L;
            DownLoadFileInfo downLoadFileInfo = new DownLoadFileInfo();
            downLoadFileInfo.setStart(start);
            downLoadFileInfo.setEnd(fileSize);
            downLoadFileInfo.setDownloadBytes(download_bytes);
            return downLoadFileInfo;
         }
      } else {
         return null;
      }
   }

   public static void setFileSize(HttpServletResponse resp, Long fileSize) {
      resp.setHeader("Content-Length", Long.toString(fileSize));
   }
}
