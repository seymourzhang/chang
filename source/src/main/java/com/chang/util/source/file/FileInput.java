package com.chang.util.source.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.TaskModeType;
import com.chang.until.QuartzTimeTask.QuartzUtil;
import java.io.BufferedReader;
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

public class FileInput implements InputSource<Object, Object>, Job {
   private static final Logger log = LoggerFactory.getLogger(FileInput.class);
   private String path;
   private String time;
   private String keyName;
   private Map<String, Object> parm;
   private volatile Boolean first = false;
   private TaskModeType taskModeType;
   private OutputSource source;
   private Function<Object, Object> function;
   private BufferedReader utf8Reader;

   public FileInput(String keyName, String path, String time, TaskModeType taskModeType, Map<String, Object> parm) {
      this.path = path;
      this.time = time;
      this.keyName = keyName;
      this.parm = parm;
      this.taskModeType = taskModeType;
   }

   public void InPut(OutputSource source, Function<Object, Object> function) {
      this.source = source;
      this.function = function;

      try {
         if (!this.first) {
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
               BufferedReader utf8Reader = FileUtil.getUtf8Reader(this.path);
               SourceContext.setExParm(this.parm);
               utf8Reader.lines().forEach((line) -> {
                  if (ObjectUtil.isNotNull(function)) {
                     source.Output(function.apply(line));
                  } else {
                     source.Output(line);
                  }

               });
               SourceContext.clearExParm();
            }

            this.first = true;
         }
      } catch (Exception var4) {
         log.error("FileInput keyName: {}", this.keyName, var4);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void execute(JobExecutionContext context) throws JobExecutionException {
      FileInput fileInput = (FileInput)context.getJobDetail().getJobDataMap().get("this");
      BufferedReader utf8Reader = FileUtil.getUtf8Reader(fileInput.getPath());
      fileInput.setUtf8Reader(utf8Reader);

      try {
         SourceContext.setExParm(fileInput.getParm());
         utf8Reader.lines().forEach((line) -> {
            if (ObjectUtil.isNotNull(fileInput.getFunction())) {
               fileInput.getSource().Output(fileInput.getFunction().apply(line));
            } else {
               fileInput.getSource().Output(line);
            }

         });
         SourceContext.clearExParm();
      } catch (Exception var5) {
         log.error("FileInput keyName: {}", fileInput.getKeyName(), var5);
      }

   }

   public void close() {
      try {
         QuartzUtil.close(this.keyName);
      } catch (Exception var3) {
         log.error("InPut", var3);
      }

      if (ObjectUtil.isNotNull(this.utf8Reader)) {
         try {
            this.utf8Reader.close();
            this.utf8Reader = null;
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   protected void finalize() throws Throwable {
      this.close();
   }

   public FileInput() {
   }

   public String getPath() {
      return this.path;
   }

   public String getTime() {
      return this.time;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public Boolean getFirst() {
      return this.first;
   }

   public TaskModeType getTaskModeType() {
      return this.taskModeType;
   }

   public OutputSource getSource() {
      return this.source;
   }

   public Function<Object, Object> getFunction() {
      return this.function;
   }

   public BufferedReader getUtf8Reader() {
      return this.utf8Reader;
   }

   public void setUtf8Reader(BufferedReader utf8Reader) {
      this.utf8Reader = utf8Reader;
   }
}
