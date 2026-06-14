package com.chang.until.license.license;

import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WindowsServerInfos extends AbstractServerInfos {
   protected List<String> getIpAddress() throws Exception {
      List<String> result = null;
      List<InetAddress> inetAddresses = this.getLocalAllInetAddress();
      if (inetAddresses != null && inetAddresses.size() > 0) {
         result = (List)inetAddresses.stream().map(InetAddress::getHostAddress).distinct().map(String::toLowerCase).collect(Collectors.toList());
      }

      return result;
   }

   protected List<String> getMacAddress() throws Exception {
      List<String> result = null;
      List<InetAddress> inetAddresses = this.getLocalAllInetAddress();
      if (inetAddresses != null && inetAddresses.size() > 0) {
         result = (List)inetAddresses.stream().map(this::getMacByInetAddress).distinct().collect(Collectors.toList());
      }

      return result;
   }

   protected String getCPUSerial() throws Exception {
      String serialNumber = "";
      Process process = Runtime.getRuntime().exec("wmic cpu get processorid");
      process.getOutputStream().close();
      Scanner scanner = new Scanner(process.getInputStream());
      if (scanner != null && scanner.hasNext()) {
         scanner.next();
      }

      if (scanner.hasNext()) {
         serialNumber = scanner.next().trim();
      }

      scanner.close();
      return serialNumber;
   }

   protected String getMainBoardSerial() throws Exception {
      String serialNumber = "";
      Process process = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
      process.getOutputStream().close();
      Scanner scanner = new Scanner(process.getInputStream());
      if (scanner != null && scanner.hasNext()) {
         scanner.next();
      }

      if (scanner.hasNext()) {
         serialNumber = scanner.next().trim();
      }

      scanner.close();
      return serialNumber;
   }
}
