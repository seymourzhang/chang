package com.chang.until.license.license;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class LinuxServerInfos extends AbstractServerInfos {
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
      String[] shell = new String[]{"/bin/bash", "-c", "dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
      Process process = Runtime.getRuntime().exec(shell);
      process.getOutputStream().close();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = reader.readLine().trim();
      if (StringUtils.isNotBlank(line)) {
         serialNumber = line;
      }

      reader.close();
      return serialNumber;
   }

   protected String getMainBoardSerial() throws Exception {
      String serialNumber = "";
      String[] shell = new String[]{"/bin/bash", "-c", "dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"};
      Process process = Runtime.getRuntime().exec(shell);
      process.getOutputStream().close();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = reader.readLine().trim();
      if (StringUtils.isNotBlank(line)) {
         serialNumber = line;
      }

      reader.close();
      return serialNumber;
   }
}
