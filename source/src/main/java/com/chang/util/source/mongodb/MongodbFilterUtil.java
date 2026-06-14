package com.chang.util.source.mongodb;

import cn.hutool.core.util.ObjectUtil;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.conversions.Bson;

public class MongodbFilterUtil {
   public static Bson andBsonCondition(List<Condition> conditions) {
      if (ObjectUtil.isNull(conditions)) {
         return null;
      } else {
         List<Bson> filters = new ArrayList();
         Iterator var2 = conditions.iterator();

         while(var2.hasNext()) {
            Condition condition = (Condition)var2.next();
            if (condition.getType().equals("in")) {
               if (!(condition.getValue() instanceof List)) {
                  throw new RuntimeException("in type not list");
               }

               filters.add(Filters.in(condition.getName(), (List)condition.getValue()));
            }

            if (condition.getType().equals("nin")) {
               if (!(condition.getValue() instanceof List)) {
                  throw new RuntimeException("in type not list");
               }

               filters.add(Filters.nin(condition.getName(), (List)condition.getValue()));
            }

            if (condition.getType().equals("gt")) {
               filters.add(Filters.gt(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("lt")) {
               filters.add(Filters.lt(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("gte")) {
               filters.add(Filters.gte(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("lte")) {
               filters.add(Filters.lte(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("ne")) {
               filters.add(Filters.ne(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("eq")) {
               filters.add(Filters.eq(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("regex")) {
               filters.add(Filters.regex(condition.getName(), (String)condition.getValue()));
            }
         }

         return Filters.and(filters);
      }
   }

   public static Bson orBsonCondition(List<Condition> conditions) {
      if (ObjectUtil.isNull(conditions)) {
         return null;
      } else {
         List<Bson> filters = new ArrayList();
         Iterator var2 = conditions.iterator();

         while(var2.hasNext()) {
            Condition condition = (Condition)var2.next();
            if (condition.getType().equals("in")) {
               filters.add(Filters.in(condition.getName(), new Object[]{condition.getValue()}));
            }

            if (condition.getType().equals("nin")) {
               filters.add(Filters.nin(condition.getName(), new Object[]{condition.getValue()}));
            }

            if (condition.getType().equals("gt")) {
               filters.add(Filters.gt(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("lt")) {
               filters.add(Filters.lt(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("gte")) {
               filters.add(Filters.gte(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("lte")) {
               filters.add(Filters.lte(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("ne")) {
               filters.add(Filters.ne(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("eq")) {
               filters.add(Filters.eq(condition.getName(), condition.getValue()));
            }

            if (condition.getType().equals("regex")) {
               filters.add(Filters.regex(condition.getName(), (String)condition.getValue()));
            }
         }

         return Filters.or(filters);
      }
   }

   public static Bson orBson(List<Bson> bsons) {
      return Filters.or(bsons);
   }

   public static Bson andBson(List<Bson> bsons) {
      return Filters.and(bsons);
   }
}
