package com.chang.binlog.event;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.SystemClock;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventHeaderV4;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.RotateEventData;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.common.SqlUtil;
import com.chang.binlog.config.SyncConfig;
import com.chang.binlog.event.handler.IEventHandler;
import com.chang.binlog.event.handler.ISqlHandler;
import com.chang.binlog.event.parser.IEventParserDispatcher;
import com.chang.binlog.event.parser.RunSqlInfo;
import com.chang.binlog.position.BinlogPositionEntity;
import com.chang.binlog.position.IPositionHandler;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiEventHandlerListener implements IEventListener {
   private final Logger log = LoggerFactory.getLogger(MultiEventHandlerListener.class);
   private SyncConfig syncConfig;
   private final List<IEventHandler> eventHandlerList = new ArrayList();
   private final List<ISqlHandler> sqlHandlerList = new ArrayList();
   private IEventParserDispatcher eventParserDispatcher;
   private IPositionHandler positionHandler;

   public void registerEventHandler(IEventHandler eventHandler) {
      this.eventHandlerList.add(eventHandler);
   }

   public void registerSqlHandler(ISqlHandler sqlHandler) {
      this.sqlHandlerList.add(sqlHandler);
   }

   public void onEvent(Event event) {
      try {
         List<EventType> excludePositionEventType = new ArrayList();
         excludePositionEventType.add(EventType.FORMAT_DESCRIPTION);
         excludePositionEventType.add(EventType.HEARTBEAT);
         EventType eventType = event.getHeader().getEventType();
         if (!excludePositionEventType.contains(eventType)) {
            BinlogPositionEntity binlogPositionEntity = new BinlogPositionEntity();
            if (event.getHeader().getEventType().equals(EventType.ROTATE)) {
               RotateEventData rotateEventData = (RotateEventData)event.getData();
               binlogPositionEntity.setBinlogName(rotateEventData.getBinlogFilename());
               binlogPositionEntity.setPosition(rotateEventData.getBinlogPosition());
               binlogPositionEntity.setServerId(event.getHeader().getServerId());
            } else {
               binlogPositionEntity = this.positionHandler.getPosition(this.syncConfig);
               EventHeaderV4 eventHeaderV4 = (EventHeaderV4)event.getHeader();
               binlogPositionEntity.setPosition(eventHeaderV4.getPosition());
               binlogPositionEntity.setServerId(event.getHeader().getServerId());
            }

            if (ObjectUtil.isNotNull(this.positionHandler)) {
               this.positionHandler.savePosition(this.syncConfig, binlogPositionEntity);
            }
         }

         List<EventEntity> eventEntityList = this.eventParserDispatcher.parse(event);
         if (CollectionUtil.isNotEmpty(eventEntityList)) {
            eventEntityList.forEach((eventEntity) -> {
               this.eventHandlerList.forEach((eventHandler) -> {
                  ThreadUtil.execute(() -> {
                     try {
                        eventHandler.process(eventEntity);
                     } catch (BinlogPortalException var4) {
                        this.log.error("process error:" + var4.getMessage(), var4);
                     }

                  });
               });
               this.sqlHandlerList.forEach((sqlHandler) -> {
                  ThreadUtil.execute(() -> {
                     try {
                        String sql = SqlUtil.event2Sql(eventEntity, this.syncConfig);
                        RunSqlInfo runSqlInfo = RunSqlInfo.builder().dbKey(this.syncConfig.getDbKey()).sqlType(eventEntity.getEventEntityType().getDesc()).sql(sql).tableName(eventEntity.getTableName()).timeStamp(SystemClock.now()).build();
                        sqlHandler.process(runSqlInfo);
                     } catch (Exception var5) {
                        this.log.error("process error:" + var5.getMessage(), var5);
                     }

                  });
               });
            });
         }
      } catch (Exception var6) {
         this.log.error(var6.getMessage(), var6);
      }

   }

   public Logger getLog() {
      return this.log;
   }

   public SyncConfig getSyncConfig() {
      return this.syncConfig;
   }

   public List<IEventHandler> getEventHandlerList() {
      return this.eventHandlerList;
   }

   public List<ISqlHandler> getSqlHandlerList() {
      return this.sqlHandlerList;
   }

   public IEventParserDispatcher getEventParserDispatcher() {
      return this.eventParserDispatcher;
   }

   public IPositionHandler getPositionHandler() {
      return this.positionHandler;
   }

   public void setSyncConfig(SyncConfig syncConfig) {
      this.syncConfig = syncConfig;
   }

   public void setEventParserDispatcher(IEventParserDispatcher eventParserDispatcher) {
      this.eventParserDispatcher = eventParserDispatcher;
   }

   public void setPositionHandler(IPositionHandler positionHandler) {
      this.positionHandler = positionHandler;
   }
}
