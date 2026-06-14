package com.chang.common.cron;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CronUtil {
   public static String dateToCron(String time) throws ParseException {
      SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat out = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
      return out.format(in.parse(time));
   }

   public static String everyDayToCron(String time) throws ParseException {
      SimpleDateFormat in = new SimpleDateFormat("HH:mm:ss");
      SimpleDateFormat out = new SimpleDateFormat("ss mm HH * * ?");
      return out.format(in.parse(time));
   }

   public static String everyMonthToCron(String time) throws ParseException {
      SimpleDateFormat in = new SimpleDateFormat("dd HH:mm:ss");
      SimpleDateFormat out = new SimpleDateFormat("ss mm HH dd * ?");
      return out.format(in.parse(time));
   }

   public static String everyWeekToCron(String time, int startWeek, int endWeek) throws ParseException {
      String format = String.format("ss mm HH ? * %s-%s", startWeek, endWeek);
      SimpleDateFormat in = new SimpleDateFormat("HH:mm:ss");
      SimpleDateFormat out = new SimpleDateFormat(format);
      return out.format(in.parse(time));
   }

   public static String everyWeekToCron(String time, int week) throws ParseException {
      String format = String.format("ss mm HH ? * %s", week);
      SimpleDateFormat in = new SimpleDateFormat("HH:mm:ss");
      SimpleDateFormat out = new SimpleDateFormat(format);
      return out.format(in.parse(time));
   }

   public static String everyHour(Integer hour) {
      return String.format("0 0 0/%s * * ?", hour);
   }

   public static String everyMinuteToCron(Integer minute) {
      return String.format("0 0/%s * * * ?", minute);
   }

   public static String everySecond(Integer second) {
      return String.format("0/%s * * * * ?", second);
   }
}
