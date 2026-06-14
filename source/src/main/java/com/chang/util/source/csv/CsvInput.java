package com.chang.util.source.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.TaskModeType;
import com.chang.util.source.file.FileInput;
import com.chang.until.QuartzTimeTask.QuartzUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvInput implements InputSource<Object, Object>, Job {
   private static final Logger log = LoggerFactory.getLogger(CsvInput.class);
   private CsvReader reader;
   private Class<?> clazz;
   private String time;
   private TaskModeType taskModeType;
   private Map<String, Object> parm;
   private String keyName;
   private OutputSource source;
   private Function<Object, Object> function;
   private String fileFullPath;

   public CsvInput(String keyName, String fileFullPath, Class<?> clazz, String time, TaskModeType taskModeType, Map<String, Object> parm) {
      this.reader = CsvUtil.getReader();
      this.clazz = clazz;
      this.parm = parm;
      this.keyName = keyName;
      this.fileFullPath = fileFullPath;
      this.time = time;
      this.taskModeType = taskModeType;
   }

   public void InPut(OutputSource source, Function<Object, Object> function) {
      this.source = source;
      this.function = function;

      try {
         if (!StringUtils.isBlank(this.time) && !ObjectUtil.isNull(this.taskModeType)) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("this", this);
            if (ObjectUtil.equals(TaskModeType.ONCE.getTaskModeType(), this.taskModeType.getTaskModeType())) {
               QuartzUtil.setStartTimeToRun(this.keyName, FileInput.class, this.time, jobDataMap);
            } else if (ObjectUtil.equals(TaskModeType.LOOP_DAY.getTaskModeType(), this.taskModeType.getTaskModeType())) {
               QuartzUtil.setEveryDayToRun(this.keyName, FileInput.class, this.time, jobDataMap);
            } else if (ObjectUtil.equals(TaskModeType.LOOP_MONTH.getTaskModeType(), this.taskModeType.getTaskModeType())) {
               QuartzUtil.setEveryMonthToRun(this.keyName, FileInput.class, this.time, jobDataMap);
            } else if (ObjectUtil.equals(TaskModeType.EVERY_HOUR.getTaskModeType(), this.taskModeType.getTaskModeType())) {
               QuartzUtil.setEveryHourToRun(this.keyName, FileInput.class, Integer.parseInt(this.time), jobDataMap);
            } else if (ObjectUtil.equals(TaskModeType.EVERY_MINUTE.getTaskModeType(), this.taskModeType.getTaskModeType())) {
               QuartzUtil.setEveryMinuteToRun(this.keyName, FileInput.class, Integer.parseInt(this.time), jobDataMap);
            } else if (ObjectUtil.equals(TaskModeType.EVERY_SECOND.getTaskModeType(), this.taskModeType.getTaskModeType())) {
               QuartzUtil.setEverySecondToRun(this.keyName, FileInput.class, Integer.parseInt(this.time), jobDataMap);
            }
         } else {
            CsvData readData = this.reader.read(FileUtil.file(this.fileFullPath));
            Iterator<CsvRow> iterator = readData.iterator();
            if (ObjectUtil.isNull(this.parm)) {
               this.parm = new HashMap();
            }

            SourceContext.setExParm(this.parm);

            while(iterator.hasNext()) {
               CsvRow row = (CsvRow)iterator.next();
               Object bean = row.toBean(this.clazz);
               if (ObjectUtil.isNotNull(function)) {
                  source.Output(function.apply(bean));
               } else {
                  source.Output(bean);
               }
            }

            SourceContext.clearExParm();
         }
      } catch (Exception var7) {
         log.error("CsvInput keyName: {}", this.keyName, var7);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void execute(JobExecutionContext context) throws JobExecutionException {
      CsvInput csvInput = (CsvInput)context.getJobDetail().getJobDataMap().get("this");
      this.reader = csvInput.getReader();
      this.parm = csvInput.getParm();
      this.source = csvInput.getSource();
      this.function = csvInput.getFunction();
      this.clazz = csvInput.getClazz();
      CsvData readData = this.reader.read(FileUtil.file(csvInput.getFileFullPath()));
      Iterator<CsvRow> iterator = readData.iterator();
      if (ObjectUtil.isNull(this.parm)) {
         this.parm = new HashMap();
      }

      SourceContext.setExParm(this.parm);

      while(iterator.hasNext()) {
         CsvRow row = (CsvRow)iterator.next();
         Object bean = row.toBean(this.clazz);
         if (ObjectUtil.isNotNull(this.function)) {
            this.source.Output(this.function.apply(bean));
         } else {
            this.source.Output(bean);
         }
      }

      SourceContext.clearExParm();
   }

   public void close() {
      try {
         QuartzUtil.close(this.keyName);
      } catch (Exception var3) {
         log.error("InPut", var3);
      }

      if (ObjectUtil.isNotNull(this.reader)) {
         try {
            this.reader.close();
            this.reader = null;
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   public CsvInput() {
   }

   public CsvReader getReader() {
      return this.reader;
   }

   public Class<?> getClazz() {
      return this.clazz;
   }

   public String getTime() {
      return this.time;
   }

   public TaskModeType getTaskModeType() {
      return this.taskModeType;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public OutputSource getSource() {
      return this.source;
   }

   public Function<Object, Object> getFunction() {
      return this.function;
   }

   public String getFileFullPath() {
      return this.fileFullPath;
   }
}
