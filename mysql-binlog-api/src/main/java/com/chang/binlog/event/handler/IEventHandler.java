package com.chang.binlog.event.handler;

import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.event.EventEntity;

public interface IEventHandler {
   void process(EventEntity var1) throws BinlogPortalException;
}
