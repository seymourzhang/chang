package com.chang.binlog.event.parser.converter;

public class StringConverter implements IConverter<String> {
   public String convert(Object from) {
      if (from == null) {
         return null;
      } else {
         return from.getClass() == byte[].class ? new String((byte[])((byte[])from)) : String.valueOf(from);
      }
   }

   public String convert(Object from, String type) {
      if (from == null) {
         return "";
      } else {
         return from.getClass() == byte[].class ? new String((byte[])((byte[])from)) : String.valueOf(from);
      }
   }
}
