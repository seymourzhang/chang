package com.chang.binlog.event.parser;

import cn.hutool.core.util.ObjectUtil;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.event.EventEntity;
import com.chang.binlog.event.EventEntityType;
import com.chang.binlog.event.parser.converter.CommonConverterProcessor;
import com.chang.binlog.tablemeta.TableMetaEntity;
import com.chang.binlog.tablemeta.TableMetaFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateEventParser implements IEventParser {
   private CommonConverterProcessor commonConverterProcessor = new CommonConverterProcessor();
   private TableMetaFactory tableMetaFactory;

   UpdateEventParser(TableMetaFactory tableMetaFactory) {
      this.tableMetaFactory = tableMetaFactory;
   }

   public List<EventEntity> parse(Event event) throws BinlogPortalException {
      List<EventEntity> eventEntityList = new ArrayList();
      UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData)event.getData();
      TableMetaEntity tableMetaEntity = this.tableMetaFactory.getTableMetaEntity(updateRowsEventData.getTableId());
      if (ObjectUtil.isNull(tableMetaEntity)) {
         return eventEntityList;
      } else {
         String tableName = tableMetaEntity.getTableName();
         List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
         rows.forEach((rowMap) -> {
            EventEntity eventEntity = new EventEntity();
            eventEntity.setEvent(event);
            eventEntity.setDatabaseName(tableMetaEntity.getDbName());
            eventEntity.setTableName(tableName);
            eventEntity.setEventEntityType(EventEntityType.UPDATE);
            List<TableMetaEntity.ColumnMetaData> columnMetaDataList = tableMetaEntity.getColumnMetaDataList();
            String[] before = this.commonConverterProcessor.convertToString((Serializable[])rowMap.getKey(), columnMetaDataList);
            String[] after = this.commonConverterProcessor.convertToString((Serializable[])rowMap.getValue(), columnMetaDataList);
            List<String> columns = new ArrayList();
            List<Object> changeBefore = new ArrayList();
            List<Object> changeAfter = new ArrayList();

            for(int i = 0; i < before.length; ++i) {
               columns.add(((TableMetaEntity.ColumnMetaData)columnMetaDataList.get(i)).getName());
               changeBefore.add(before[i]);
               changeAfter.add(after[i]);
            }

            eventEntity.setColumns(columnMetaDataList);
            eventEntity.setChangeBefore(changeBefore);
            eventEntity.setChangeAfter(changeAfter);
            eventEntityList.add(eventEntity);
         });
         return eventEntityList;
      }
   }
}
