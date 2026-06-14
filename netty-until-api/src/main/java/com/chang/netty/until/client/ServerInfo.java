package com.chang.netty.until.client;

import io.netty.channel.ChannelHandlerContext;

public class ServerInfo {
   String serverAddr;
   ChannelHandlerContext ctx;

   ServerInfo(String serverAddr, ChannelHandlerContext ctx) {
      this.serverAddr = serverAddr;
      this.ctx = ctx;
   }

   public static ServerInfoBuilder builder() {
      return new ServerInfoBuilder();
   }

   public void setServerAddr(String serverAddr) {
      this.serverAddr = serverAddr;
   }

   public void setCtx(ChannelHandlerContext ctx) {
      this.ctx = ctx;
   }

   public String getServerAddr() {
      return this.serverAddr;
   }

   public ChannelHandlerContext getCtx() {
      return this.ctx;
   }

   public static class ServerInfoBuilder {
      private String serverAddr;
      private ChannelHandlerContext ctx;

      ServerInfoBuilder() {
      }

      public ServerInfoBuilder serverAddr(String serverAddr) {
         this.serverAddr = serverAddr;
         return this;
      }

      public ServerInfoBuilder ctx(ChannelHandlerContext ctx) {
         this.ctx = ctx;
         return this;
      }

      public ServerInfo build() {
         return new ServerInfo(this.serverAddr, this.ctx);
      }

      public String toString() {
         return "ServerInfo.ServerInfoBuilder(serverAddr=" + this.serverAddr + ", ctx=" + this.ctx + ")";
      }
   }
}
