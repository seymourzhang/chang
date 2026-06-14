package com.chang.uclass.bytecode;

import java.io.File;

public class Constants {
   public static final String Q_OR_CTRL_C_ABORT_MSG = "Press Q or Ctrl+C to abort.";
   public static final String EMPTY_STRING = "";
   public static final String DEFAULT_PROMPT = "$ ";
   public static final String COST_VARIABLE = "cost";
   public static final String CMD_HISTORY_FILE;

   private Constants() {
   }

   static {
      CMD_HISTORY_FILE = System.getProperty("user.home") + File.separator + ".arthas" + File.separator + "history";
   }
}
