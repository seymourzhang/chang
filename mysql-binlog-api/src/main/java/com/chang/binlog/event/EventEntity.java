package com.chang.binlog.event;

import com.github.shyiko.mysql.binlog.event.Event;
import com.chang.binlog.tablemeta.TableMetaEntity;
import com.chang.common.CommUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventEntity {
   private static final Logger log = LoggerFactory.getLogger(EventEntity.class);
   Event event;
   EventEntityType eventEntityType;
   String databaseName;
   String tableName;
   List<TableMetaEntity.ColumnMetaData> columns;
   List<Object> changeBefore;
   List<Object> changeAfter;

   public String getJsonFormatData() {
      Map<String, Object> params = new HashMap();
      params.put("change_type", this.getEventEntityType().getDesc());
      Map<String, String[]> data = new HashMap();

      for(int i = 0; i < this.getChangeAfter().size(); ++i) {
         String before = "";
         if (this.getChangeBefore() != null) {
            before = this.getChangeBefore().get(i) != null ? this.getChangeBefore().get(i).toString() : "";
         }

         String after = "";
         if (this.getChangeAfter() != null) {
            after = this.getChangeAfter().get(i) != null ? this.getChangeAfter().get(i).toString() : "";
         }

         String[] subData = new String[]{before, after, Objects.equals(before, after) ? "0" : "1"};
         data.put(((TableMetaEntity.ColumnMetaData)this.getColumns().get(i)).getName(), subData);
      }

      params.put("change_data", data);
      params.put("table_name", this.getTableName());
      return CommUtils.getJSONStringFromObject(params);
   }

   public Event getEvent() {
      return this.event;
   }

   public EventEntityType getEventEntityType() {
      return this.eventEntityType;
   }

   public String getDatabaseName() {
      return this.databaseName;
   }

   public String getTableName() {
      return this.tableName;
   }

   public List<TableMetaEntity.ColumnMetaData> getColumns() {
      return this.columns;
   }

   public List<Object> getChangeBefore() {
      return this.changeBefore;
   }

   public List<Object> getChangeAfter() {
      return this.changeAfter;
   }

   public void setEvent(Event event) {
      this.event = event;
   }

   public void setEventEntityType(EventEntityType eventEntityType) {
      this.eventEntityType = eventEntityType;
   }

   public void setDatabaseName(String databaseName) {
      this.databaseName = databaseName;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public void setColumns(List<TableMetaEntity.ColumnMetaData> columns) {
      this.columns = columns;
   }

   public void setChangeBefore(List<Object> changeBefore) {
      this.changeBefore = changeBefore;
   }

   public void setChangeAfter(List<Object> changeAfter) {
      this.changeAfter = changeAfter;
   }

   public String toString() {
      return "EventEntity(event=" + this.getEvent() + ", eventEntityType=" + this.getEventEntityType() + ", databaseName=" + this.getDatabaseName() + ", tableName=" + this.getTableName() + ", columns=" + this.getColumns() + ", changeBefore=" + this.getChangeBefore() + ", changeAfter=" + this.getChangeAfter() + ")";
   }
}
