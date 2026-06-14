package com.chang.util.mybatis.ddl;

import com.baomidou.mybatisplus.extension.ddl.SimpleDdl;
import java.util.Arrays;
import java.util.List;

public class BaseDdl extends SimpleDdl {
   private String[] sqlFilesPath;

   public BaseDdl(String[] sqlFiles) {
      this.sqlFilesPath = sqlFiles;
   }

   public List<String> getSqlFiles() {
      return Arrays.asList(this.sqlFilesPath);
   }
}
