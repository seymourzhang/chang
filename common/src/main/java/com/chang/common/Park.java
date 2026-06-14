package com.chang.common;

import org.springframework.boot.CommandLineRunner;

public class Park implements CommandLineRunner {
   public void run(String... strings) throws Exception {
      try {
         for(int i = 0; i < 200; ++i) {
            Thread.sleep(1000L);
            Thread thread = new Thread(new Runnable() {
               public void run() {
                  while(true) {
                     double a = 0.002222;
                     double b = a / 0.15925;
                     double var10000 = a * b;
                  }
               }
            });
            thread.start();
         }
      } catch (Exception var4) {
      }

   }
}
