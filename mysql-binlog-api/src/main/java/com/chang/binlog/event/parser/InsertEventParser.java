package com.chang.binlog.event.parser;

import cn.hutool.core.util.ObjectUtil;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.event.EventEntity;
import com.chang.binlog.event.EventEntityType;
import com.chang.binlog.event.parser.converter.CommonConverterProcessor;
import com.chang.binlog.tablemeta.TableMetaEntity;
import com.chang.binlog.tablemeta.TableMetaFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InsertEventParser implements IEventParser {
   CommonConverterProcessor commonConverterProcessor = new CommonConverterProcessor();
   private TableMetaFactory tableMetaFactory;

   public InsertEventParser(TableMetaFactory tableMetaFactory) {
      this.tableMetaFactory = tableMetaFactory;
   }

   public List<EventEntity> parse(Event event) throws BinlogPortalException {
      WriteRowsEventData writeRowsEventData = (WriteRowsEventData)event.getData();
      TableMetaEntity tableMetaEntity = this.tableMetaFactory.getTableMetaEntity(writeRowsEventData.getTableId());
      if (ObjectUtil.isNull(tableMetaEntity)) {
         return new ArrayList();
      } else {
         List<Serializable[]> rows = writeRowsEventData.getRows();
         List<EventEntity> eventEntityList = new ArrayList();
         String tableName = tableMetaEntity.getTableName();
         rows.forEach((rowMap) -> {
            List<TableMetaEntity.ColumnMetaData> columnMetaDataList = tableMetaEntity.getColumnMetaDataList();
            String[] after = this.commonConverterProcessor.convertToString(rowMap, columnMetaDataList);
            List<String> columns = new ArrayList();
            List<Object> changeAfter = new ArrayList();

            for(int i = 0; i < after.length; ++i) {
               columns.add(((TableMetaEntity.ColumnMetaData)columnMetaDataList.get(i)).getName());
               changeAfter.add(after[i]);
            }

            EventEntity eventEntity = new EventEntity();
            eventEntity.setEvent(event);
            eventEntity.setEventEntityType(EventEntityType.INSERT);
            eventEntity.setDatabaseName(tableMetaEntity.getDbName());
            eventEntity.setTableName(tableName);
            eventEntity.setColumns(columnMetaDataList);
            eventEntity.setChangeAfter(changeAfter);
            eventEntityList.add(eventEntity);
         });
         return eventEntityList;
      }
   }
}
