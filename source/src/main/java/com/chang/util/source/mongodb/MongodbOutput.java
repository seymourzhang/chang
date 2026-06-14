package com.chang.util.source.mongodb;

import cn.hutool.core.bean.BeanUtil;
import com.chang.util.source.OutputSource;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bson.Document;

public class MongodbOutput implements OutputSource {
   private final MongoClient client;
   private final MongoDatabase dataBase;
   private final MongoCollection<Document> collection;
   private final String keyName;
   private final Map<String, Object> parm;

   public MongodbOutput(String url, String databaseName, String collectionName, String keyName, Map<String, Object> parm) {
      this.client = MongoClients.create(url);
      this.dataBase = this.client.getDatabase(databaseName);
      this.collection = this.dataBase.getCollection(collectionName);
      this.keyName = keyName;
      this.parm = parm;
   }

   public void Output(Object o) {
      Iterator var4;
      if (BeanUtil.isBean(o.getClass())) {
         Map<String, Object> map = BeanUtil.beanToMap(o, (String[])null);
         Document doc = new Document();
         var4 = map.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var4.next();
            if (((String)entry.getKey()).equals("id")) {
               doc.put("_id", entry.getValue());
            } else {
               doc.append((String)entry.getKey(), entry.getValue());
            }
         }

         this.collection.insertOne(doc);
      } else {
         if (!(o instanceof List)) {
            throw new RuntimeException("data is not bean or list");
         }

         List dataList = (List)o;
         List<Document> docList = new ArrayList();
         var4 = dataList.iterator();

         while(var4.hasNext()) {
            Object data = var4.next();
            if (!BeanUtil.isBean(data.getClass())) {
               throw new RuntimeException("List in data is not bean or list");
            }

            Map<String, Object> mapFromList = BeanUtil.beanToMap(data, (String[])null);
            Document docFromList = new Document();
            Iterator var8 = mapFromList.entrySet().iterator();

            while(var8.hasNext()) {
               Map.Entry<String, Object> entry = (Map.Entry)var8.next();
               if (((String)entry.getKey()).equals("id")) {
                  docFromList.put("_id", entry.getValue());
               } else {
                  docFromList.append((String)entry.getKey(), entry.getValue());
               }
            }

            docList.add(docFromList);
         }

         this.collection.insertMany(docList);
      }

   }

   public Map<String, Object> getSourceExParm() {
      return this.parm;
   }

   public void close() {
      this.client.close();
   }

   protected void finalize() throws Throwable {
      this.close();
   }
}
