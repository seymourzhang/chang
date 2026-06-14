package com.chang.util.source.mongodb;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.chang.until.QuartzTimeTask.QuartzUtil;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.TaskModeType;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongodbInput implements InputSource<JSONObject, Object>, Job {
   private static final Logger log = LoggerFactory.getLogger(MongodbInput.class);
   private MongoCollection<Document> collection;
   private volatile List<Condition> conditions;
   private String keyName;
   private String time;
   private volatile Boolean first = false;
   private Map<String, Object> parm;
   private TaskModeType taskModeType;
   private OutputSource source;
   private Function<JSONObject, Object> function;
   private MongoClient mongoClient;
   private Bson filter;
   private String databaseName;
   private String collectionName;

   public MongodbInput(String url, String databaseName, String collectionName, List<Condition> conditions, String keyName, String time, TaskModeType taskModeType, Map<String, Object> parm) {
      this.mongoClient = MongoClients.create(url);
      this.collection = this.mongoClient.getDatabase(databaseName).getCollection(collectionName);
      this.conditions = conditions;
      this.keyName = keyName;
      this.time = time;
      this.parm = parm;
      this.taskModeType = taskModeType;
      this.databaseName = databaseName;
      this.collectionName = collectionName;
   }

   public void setMongoDb(String databaseName, String collectionName) {
      if (StringUtils.isBlank(databaseName)) {
         this.collection = this.mongoClient.getDatabase(this.databaseName).getCollection(collectionName);
      } else if (StringUtils.isBlank(collectionName)) {
         this.collection = this.mongoClient.getDatabase(databaseName).getCollection(this.collectionName);
      } else {
         this.collection = this.mongoClient.getDatabase(databaseName).getCollection(collectionName);
      }

   }

   public void InPut(OutputSource source, Function<JSONObject, Object> function) {
      try {
         this.source = source;
         this.function = function;
         if (!this.first) {
            if (!StringUtils.isBlank(this.time) && !ObjectUtil.isNull(this.taskModeType)) {
               JobDataMap jobDataMap = new JobDataMap();
               jobDataMap.put("this", this);
               if (ObjectUtil.equals(TaskModeType.ONCE.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setStartTimeToRun(this.keyName, MongodbInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.LOOP_DAY.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryDayToRun(this.keyName, MongodbInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.LOOP_MONTH.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryMonthToRun(this.keyName, MongodbInput.class, this.time, jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_HOUR.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryHourToRun(this.keyName, MongodbInput.class, Integer.parseInt(this.time), jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_MINUTE.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEveryMinuteToRun(this.keyName, MongodbInput.class, Integer.parseInt(this.time), jobDataMap);
               } else if (ObjectUtil.equals(TaskModeType.EVERY_SECOND.getTaskModeType(), this.taskModeType.getTaskModeType())) {
                  QuartzUtil.setEverySecondToRun(this.keyName, MongodbInput.class, Integer.parseInt(this.time), jobDataMap);
               }
            } else {
               try {
                  if (ObjectUtil.isNull(this.filter)) {
                     this.filter = MongodbFilterUtil.andBsonCondition(this.conditions);
                  }

                  FindIterable documents;
                  long total;
                  if (ObjectUtil.isNotNull(this.filter)) {
                     total = this.collection.countDocuments(this.filter);
                     documents = this.collection.find(this.filter);
                  } else {
                     total = this.collection.countDocuments();
                     documents = this.collection.find();
                  }

                  if (ObjectUtil.isNull(this.parm)) {
                     this.parm = new HashMap();
                  }

                  this.parm.put("mongodb_input_total_key", total);
                  SourceContext.setExParm(this.parm);

                  try {
                     MongoCursor<Document> iterator = documents.iterator();
                     Throwable var7 = null;

                     try {
                        while(iterator.hasNext()) {
                           JSONObject object = new JSONObject();
                           Document document = (Document)iterator.next();
                           object.putAll(document);
                           if (ObjectUtil.isNotNull(function)) {
                              source.Output(function.apply(object));
                           } else {
                              source.Output(object);
                           }
                        }
                     } catch (Throwable var30) {
                        var7 = var30;
                        throw var30;
                     } finally {
                        if (iterator != null) {
                           if (var7 != null) {
                              try {
                                 iterator.close();
                              } catch (Throwable var29) {
                                 var7.addSuppressed(var29);
                              }
                           } else {
                              iterator.close();
                           }
                        }

                     }
                  } catch (Exception var32) {
                     log.error("MongodbInput keyName: {}", this.keyName, var32);
                  }
               } catch (Throwable var33) {
                  log.error("MongodbInput keyName: {}", this.keyName, var33);
               }
            }

            this.first = true;
         }
      } catch (Exception var34) {
         log.error("InPut Err", var34);
         throw new RuntimeException(var34);
      } finally {
         SourceContext.clearExParm();
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void execute(JobExecutionContext context) throws JobExecutionException {
      try {
         MongodbInput mongodbInput = (MongodbInput)context.getJobDetail().getJobDataMap().get("this");
         this.filter = mongodbInput.getFilter();
         this.parm = mongodbInput.getParm();
         if (ObjectUtil.isNull(this.filter)) {
            this.filter = MongodbFilterUtil.andBsonCondition(mongodbInput.getConditions());
         }

         FindIterable documents;
         long total;
         if (ObjectUtil.isNotNull(this.filter)) {
            documents = mongodbInput.getCollection().find(this.filter);
            total = mongodbInput.getCollection().countDocuments(this.filter);
         } else {
            documents = mongodbInput.getCollection().find();
            total = mongodbInput.getCollection().countDocuments();
         }

         if (ObjectUtil.isNull(this.parm)) {
            this.parm = new HashMap();
         }

         this.parm.put("mongodb_input_total_key", total);
         SourceContext.setExParm(this.parm);

         try {
            MongoCursor<Document> iterator = documents.iterator();
            Throwable var7 = null;

            try {
               while(iterator.hasNext()) {
                  JSONObject object = new JSONObject();
                  Document document = (Document)iterator.next();
                  object.putAll(document);
                  if (ObjectUtil.isNotNull(mongodbInput.getFunction())) {
                     mongodbInput.getSource().Output(mongodbInput.getFunction().apply(object));
                  } else {
                     mongodbInput.getSource().Output(object);
                  }
               }
            } catch (Throwable var28) {
               var7 = var28;
               throw var28;
            } finally {
               if (iterator != null) {
                  if (var7 != null) {
                     try {
                        iterator.close();
                     } catch (Throwable var27) {
                        var7.addSuppressed(var27);
                     }
                  } else {
                     iterator.close();
                  }
               }

            }
         } catch (Exception var30) {
            log.error("MongodbInput keyName: {}", mongodbInput.getKeyName(), var30);
         }
      } catch (Exception var31) {
         log.error("MongodbInput Err", var31);
      } finally {
         SourceContext.clearExParm();
      }

   }

   public void close() {
      try {
         QuartzUtil.close(this.keyName);
      } catch (Exception var2) {
         log.error("InPut", var2);
      }

      if (ObjectUtil.isNotNull(this.mongoClient)) {
         this.mongoClient.close();
      }

   }

   protected void finalize() throws Throwable {
      this.close();
   }

   public MongodbInput() {
   }

   public MongoCollection<Document> getCollection() {
      return this.collection;
   }

   public List<Condition> getConditions() {
      return this.conditions;
   }

   public void setConditions(List<Condition> conditions) {
      this.conditions = conditions;
   }

   public String getKeyName() {
      return this.keyName;
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

   public MongoClient getMongoClient() {
      return this.mongoClient;
   }

   public Bson getFilter() {
      return this.filter;
   }

   public void setFilter(Bson filter) {
      this.filter = filter;
   }
}
