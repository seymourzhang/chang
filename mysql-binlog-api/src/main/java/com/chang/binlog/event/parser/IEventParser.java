package com.chang.binlog.event.parser;

import com.github.shyiko.mysql.binlog.event.Event;
import com.chang.binlog.BinlogPortalException;
import com.chang.binlog.event.EventEntity;
import java.util.List;

public interface IEventParser {
   List<EventEntity> parse(Event var1) throws BinlogPortalException;
}
