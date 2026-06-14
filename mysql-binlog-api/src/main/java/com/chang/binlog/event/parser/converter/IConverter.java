package com.chang.binlog.event.parser.converter;

public interface IConverter<T> {
   T convert(Object var1);

   T convert(Object var1, String var2);
}
