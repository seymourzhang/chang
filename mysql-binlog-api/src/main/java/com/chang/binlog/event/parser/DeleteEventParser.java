package com.chang.binlog.event.parser;

import cn.hutool.core.util.ObjectUtil;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.event.EventEntity;
import com.chang.binlog.event.EventEntityType;
import com.chang.binlog.event.parser.converter.CommonConverterProcessor;
import com.chang.binlog.tablemeta.TableMetaEntity;
import com.chang.binlog.tablemeta.TableMetaFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeleteEventParser implements IEventParser {
   private CommonConverterProcessor commonConverterProcessor = new CommonConverterProcessor();
   private TableMetaFactory tableMetaFactory;

   public DeleteEventParser(TableMetaFactory tableMetaFactory) {
      this.tableMetaFactory = tableMetaFactory;
   }

   public List<EventEntity> parse(Event event) throws BinlogPortalException {
      List<EventEntity> eventEntityList = new ArrayList();
      DeleteRowsEventData deleteRowsEventData = (DeleteRowsEventData)event.getData();
      TableMetaEntity tableMetaEntity = this.tableMetaFactory.getTableMetaEntity(deleteRowsEventData.getTableId());
      if (ObjectUtil.isNull(tableMetaEntity)) {
         return eventEntityList;
      } else {
         List<Serializable[]> rows = deleteRowsEventData.getRows();
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
            eventEntity.setEventEntityType(EventEntityType.DELETE);
            eventEntity.setDatabaseName(tableMetaEntity.getDbName());
            eventEntity.setTableName(tableMetaEntity.getTableName());
            eventEntity.setColumns(columnMetaDataList);
            eventEntity.setChangeAfter(changeAfter);
            eventEntityList.add(eventEntity);
         });
         return eventEntityList;
      }
   }
}
