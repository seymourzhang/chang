package com.chang.binlog.position;

public class BinlogPositionEntity {
   private String binlogName;
   private Long position;
   private Long serverId;

   public String getBinlogName() {
      return this.binlogName;
   }

   public Long getPosition() {
      return this.position;
   }

   public Long getServerId() {
      return this.serverId;
   }

   public void setBinlogName(String binlogName) {
      this.binlogName = binlogName;
   }

   public void setPosition(Long position) {
      this.position = position;
   }

   public void setServerId(Long serverId) {
      this.serverId = serverId;
   }

   public String toString() {
      return "BinlogPositionEntity(binlogName=" + this.getBinlogName() + ", position=" + this.getPosition() + ", serverId=" + this.getServerId() + ")";
   }
}
