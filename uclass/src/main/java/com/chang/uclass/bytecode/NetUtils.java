package com.chang.uclass.bytecode;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {
   public static String getIpByHost(String hostName) {
      try {
         return InetAddress.getByName(hostName).getHostAddress();
      } catch (UnknownHostException var2) {
         return hostName;
      }
   }
}
