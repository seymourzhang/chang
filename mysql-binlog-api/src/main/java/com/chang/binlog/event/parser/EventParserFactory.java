package com.chang.binlog.event.parser;

import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.config.SyncConfig;
import com.chang.binlog.tablemeta.TableMetaFactory;

public class EventParserFactory {
   public static IEventParserDispatcher getEventParserDispatcher(SyncConfig syncConfig) throws BinlogPortalException {
      return new CommonEventParserDispatcher(new TableMetaFactory(syncConfig));
   }
}
