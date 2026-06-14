package com.chang.netty.until.server;

import io.netty.channel.ChannelHandlerContext;

public class ClientInfo {
   private String name;
   private String clientAddr;
   private ChannelHandlerContext ctx;

   public static ClientInfoBuilder builder() {
      return new ClientInfoBuilder();
   }

   public String toString() {
      return "ClientInfo(name=" + this.getName() + ", clientAddr=" + this.getClientAddr() + ", ctx=" + this.getCtx() + ")";
   }

   public String getName() {
      return this.name;
   }

   public String getClientAddr() {
      return this.clientAddr;
   }

   public ChannelHandlerContext getCtx() {
      return this.ctx;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setClientAddr(String clientAddr) {
      this.clientAddr = clientAddr;
   }

   public void setCtx(ChannelHandlerContext ctx) {
      this.ctx = ctx;
   }

   public ClientInfo(String name, String clientAddr, ChannelHandlerContext ctx) {
      this.name = name;
      this.clientAddr = clientAddr;
      this.ctx = ctx;
   }

   public ClientInfo() {
   }

   public static class ClientInfoBuilder {
      private String name;
      private String clientAddr;
      private ChannelHandlerContext ctx;

      ClientInfoBuilder() {
      }

      public ClientInfoBuilder name(String name) {
         this.name = name;
         return this;
      }

      public ClientInfoBuilder clientAddr(String clientAddr) {
         this.clientAddr = clientAddr;
         return this;
      }

      public ClientInfoBuilder ctx(ChannelHandlerContext ctx) {
         this.ctx = ctx;
         return this;
      }

      public ClientInfo build() {
         return new ClientInfo(this.name, this.clientAddr, this.ctx);
      }

      public String toString() {
         return "ClientInfo.ClientInfoBuilder(name=" + this.name + ", clientAddr=" + this.clientAddr + ", ctx=" + this.ctx + ")";
      }
   }
}
