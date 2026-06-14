package com.chang.binlog.event.parser;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.event.EventEntity;
import com.chang.binlog.tablemeta.TableMetaFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonEventParserDispatcher implements IEventParserDispatcher {
   private static final Logger log = LoggerFactory.getLogger(CommonEventParserDispatcher.class);
   private TableMetaFactory tableMetaFactory;
   IEventParser updateEventParser;
   IEventParser insertEventParser;
   IEventParser deleteEventParser;

   public TableMetaFactory getTableMetaFactory() {
      return this.tableMetaFactory;
   }

   public void setTableMetaFactory(TableMetaFactory tableMetaFactory) {
      this.tableMetaFactory = tableMetaFactory;
   }

   public CommonEventParserDispatcher(TableMetaFactory tableMetaFactory) {
      this.tableMetaFactory = tableMetaFactory;
      this.updateEventParser = new UpdateEventParser(tableMetaFactory);
      this.insertEventParser = new InsertEventParser(tableMetaFactory);
      this.deleteEventParser = new DeleteEventParser(tableMetaFactory);
   }

   public List<EventEntity> parse(Event event) throws BinlogPortalException {
      if (EventType.TABLE_MAP.equals(event.getHeader().getEventType())) {
         TableMapEventData tableMapEventData = (TableMapEventData)event.getData();
         this.tableMetaFactory.getTableMetaEntity(tableMapEventData.getTableId(), tableMapEventData.getDatabase(), tableMapEventData.getTable());
      }

      if (EventType.isUpdate(event.getHeader().getEventType())) {
         return this.updateEventParser.parse(event);
      } else if (EventType.isWrite(event.getHeader().getEventType())) {
         return this.insertEventParser.parse(event);
      } else {
         return EventType.isDelete(event.getHeader().getEventType()) ? this.deleteEventParser.parse(event) : null;
      }
   }
}
