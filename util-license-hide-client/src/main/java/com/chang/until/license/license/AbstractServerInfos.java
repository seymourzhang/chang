package com.chang.until.license.license;

import com.chang.common.LogUtil;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public abstract class AbstractServerInfos {
   private static final LogUtil logger = LogUtil.getLogger(AbstractServerInfos.class);

   public LicenseCheckModel getServerInfos() {
      LicenseCheckModel result = new LicenseCheckModel();

      try {
         result.setIpAddress(this.getIpAddress());
         result.setMacAddress(this.getMacAddress());
         result.setCpuSerial(this.getCPUSerial());
         result.setMainBoardSerial(this.getMainBoardSerial());
      } catch (Exception var3) {
         logger.error("获取服务器硬件信息失败", (Throwable)var3);
      }

      return result;
   }

   protected abstract List<String> getIpAddress() throws Exception;

   protected abstract List<String> getMacAddress() throws Exception;

   protected abstract String getCPUSerial() throws Exception;

   protected abstract String getMainBoardSerial() throws Exception;

   protected List<InetAddress> getLocalAllInetAddress() throws Exception {
      List<InetAddress> result = new ArrayList(4);
      Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

      while(networkInterfaces.hasMoreElements()) {
         NetworkInterface iface = (NetworkInterface)networkInterfaces.nextElement();
         Enumeration inetAddresses = iface.getInetAddresses();

         while(inetAddresses.hasMoreElements()) {
            InetAddress inetAddr = (InetAddress)inetAddresses.nextElement();
            if (!inetAddr.isLoopbackAddress() && !inetAddr.isLinkLocalAddress() && !inetAddr.isMulticastAddress()) {
               result.add(inetAddr);
            }
         }
      }

      return result;
   }

   protected String getMacByInetAddress(InetAddress inetAddr) {
      try {
         byte[] mac = NetworkInterface.getByInetAddress(inetAddr).getHardwareAddress();
         StringBuffer stringBuffer = new StringBuffer();

         for(int i = 0; i < mac.length; ++i) {
            if (i != 0) {
               stringBuffer.append("-");
            }

            String temp = Integer.toHexString(mac[i] & 255);
            if (temp.length() == 1) {
               stringBuffer.append("0" + temp);
            } else {
               stringBuffer.append(temp);
            }
         }

         return stringBuffer.toString().toUpperCase();
      } catch (SocketException var6) {
         var6.printStackTrace();
         return null;
      }
   }
}
