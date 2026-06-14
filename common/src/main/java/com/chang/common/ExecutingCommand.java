package com.chang.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutingCommand {
   private static final Logger log = LoggerFactory.getLogger(ExecutingCommand.class);

   private ExecutingCommand() {
   }

   public static List<String> runNative(String cmdToRun) {
      String[] cmd = cmdToRun.split(" ");
      return runNative(cmd);
   }

   public static List<String> runNative(String[] cmdToRunWithArgs) {
      Process p = null;

      try {
         p = Runtime.getRuntime().exec(cmdToRunWithArgs);
      } catch (SecurityException var12) {
         log.error("Couldn't run command {}:", Arrays.toString(cmdToRunWithArgs), var12);
         return new ArrayList(0);
      } catch (IOException var13) {
         log.error("Couldn't run command {}:", Arrays.toString(cmdToRunWithArgs), var13);
         return new ArrayList(0);
      }

      ArrayList<String> sa = new ArrayList();
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      ArrayList var5;
      try {
         String line;
         while((line = reader.readLine()) != null) {
            sa.add(line);
         }

         p.waitFor();
         return sa;
      } catch (IOException var14) {
         log.error("Problem reading output from {}:", Arrays.toString(cmdToRunWithArgs), var14);
         var5 = new ArrayList(0);
      } catch (InterruptedException var15) {
         log.error("Problem reading output from {}:", Arrays.toString(cmdToRunWithArgs), var15);
         Thread.currentThread().interrupt();
         return sa;
      } finally {
         IOUtils.close((Reader)reader);
      }

      return var5;
   }

   public static String getFirstAnswer(String cmd2launch) {
      return getAnswerAt(cmd2launch, 0);
   }

   public static String getAnswerAt(String cmd2launch, int answerIdx) {
      List<String> sa = runNative(cmd2launch);
      return answerIdx >= 0 && answerIdx < sa.size() ? (String)sa.get(answerIdx) : "";
   }
}
