package com.chang.util.source.mysql;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSONObject;
import com.chang.util.source.common.DataBaseApi;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.TaskModeType;
import com.chang.util.source.manage.ManageMySqlSource;
import com.chang.until.QuartzTimeTask.QuartzUtil;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlInput implements InputSource<JSONObject, Object>, Job {
   private static final Logger log = LoggerFactory.getLogger(MySqlInput.class);
   private volatile String sql;
   private DataBaseApi dataBaseApi;
   private final List<String> filed = new ArrayList();
   private String keyName;
   private String time;
   private volatile Boolean first = false;
   private Map<String, Object> parm;
   private OutputSource source;
   private Function<JSONObject, Object> function;
   private TaskModeType taskModeType;

   public MySqlInput(String sql, String sourceName, String keyName, String time, TaskModeType taskModeType) {
      this.sql = sql;
      this.keyName = keyName;
      this.dataBaseApi = ManageMySqlSource.getDbSource(sourceName);
      this.time = time;
      this.taskModeType = taskModeType;
   }

   public void addExcludeFiled(String filedName) {
      this.filed.add(filedName);
   }

   public void InPut(OutputSource source, Function<JSONObject, Object> function) throws Exception {
      try {
         this.source = source;
         this.function = function;
         if (!this.first) {
            if (!StringUtils.isBlank(this.time) && !ObjectUtil.isNull(this.taskModeType)) {
               JobDataMap jobDataMap = new JobDataMap();
               jobDataMap.put("this", this);
               if (ObjectUtil.equals(TaskModeType.ONCE.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setStartTimeToRun(this.keyName, MySqlInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.LOOP_DAY.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryDayToRun(this.keyName, MySqlInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.LOOP_MONTH.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryMonthToRun(this.keyName, MySqlInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_HOUR.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryHourToRun(this.keyName, MySqlInput.class, Integer.parseInt(this.time), jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_MINUTE.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryMinuteToRun(this.keyName, MySqlInput.class, Integer.parseInt(this.time), jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_SECOND.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEverySecondToRun(this.keyName, MySqlInput.class, Integer.parseInt(this.time), jobDataMap);
               }
            } else {
               Db db = this.dataBaseApi.getDb();

               try {
                  SourceContext.setExParm(this.parm);
                  List<Entity> result = db.query(this.sql, new Object[0]);
                  Iterator var5 = result.iterator();

                  while(var5.hasNext()) {
                     Entity entity = (Entity)var5.next();
                     JSONObject res = new JSONObject();
                     Set<String> fieldNames = entity.getFieldNames();
                     Iterator var9 = fieldNames.iterator();

                     while(var9.hasNext()) {
                        String key = (String)var9.next();
                        if (!this.filed.contains(key)) {
                           res.put(key, entity.get(key));
                        }
                     }

                     if (ObjectUtil.isNotNull(function)) {
                        source.Output(function.apply(res));
                     } else {
                        source.Output(res);
                     }
                  }
               } catch (SQLException var11) {
                  log.error("sql err", var11);
               }

               SourceContext.clearExParm();
            }

            this.first = true;
         }

      } catch (Throwable var12) {
         throw var12;
      }
   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void execute(JobExecutionContext context) throws JobExecutionException {
      MySqlInput mySqlInput = (MySqlInput)context.getJobDetail().getJobDataMap().get("this");
      Db db = mySqlInput.getDataBaseApi().getDb();

      try {
         SourceContext.setExParm(mySqlInput.getParm());
         List<Entity> result = db.query(mySqlInput.getSql(), new Object[0]);
         Iterator var5 = result.iterator();

         while(var5.hasNext()) {
            Entity entity = (Entity)var5.next();
            JSONObject res = new JSONObject();
            Set<String> fieldNames = entity.getFieldNames();
            Iterator var9 = fieldNames.iterator();

            while(var9.hasNext()) {
               String key = (String)var9.next();
               if (!mySqlInput.getFiled().contains(key)) {
                  res.put(key, entity.get(key));
               }
            }

            if (ObjectUtil.isNotNull(mySqlInput.getFunction())) {
               mySqlInput.getSource().Output(mySqlInput.getFunction().apply(res));
            } else {
               mySqlInput.getSource().Output(res);
            }
         }

         SourceContext.clearExParm();
      } catch (SQLException var11) {
         log.error("sql err", var11);
      }

   }

   public void close() {
      try {
         QuartzUtil.close(this.keyName);
      } catch (Exception var3) {
         log.error("InPut", var3);
      }

      if (ObjectUtil.isNotNull(this.dataBaseApi)) {
         try {
            this.dataBaseApi.close();
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   protected void finalize() throws Throwable {
      this.close();
   }

   public String getSql() {
      return this.sql;
   }

   public DataBaseApi getDataBaseApi() {
      return this.dataBaseApi;
   }

   public List<String> getFiled() {
      return this.filed;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public String getTime() {
      return this.time;
   }

   public Boolean getFirst() {
      return this.first;
   }

   public Map<String, Object> getParm() {
      return this.parm;
   }

   public OutputSource getSource() {
      return this.source;
   }

   public Function<JSONObject, Object> getFunction() {
      return this.function;
   }

   public TaskModeType getTaskModeType() {
      return this.taskModeType;
   }

   public void setSql(String sql) {
      this.sql = sql;
   }

   public void setDataBaseApi(DataBaseApi dataBaseApi) {
      this.dataBaseApi = dataBaseApi;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public void setFirst(Boolean first) {
      this.first = first;
   }

   public void setParm(Map<String, Object> parm) {
      this.parm = parm;
   }

   public void setSource(OutputSource source) {
      this.source = source;
   }

   public void setFunction(Function<JSONObject, Object> function) {
      this.function = function;
   }

   public void setTaskModeType(TaskModeType taskModeType) {
      this.taskModeType = taskModeType;
   }

   public MySqlInput() {
   }
}
