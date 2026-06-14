package com.chang.binlog.event.handler;

import com.chang.binlog.event.parser.RunSqlInfo;

public interface ISqlHandler {
   void process(RunSqlInfo var1);
}
