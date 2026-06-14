package com.chang.common.util;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONObject;
import com.chang.common.annotations.ExcelColumn;
import com.chang.common.annotations.ExcelModel;
import com.google.common.collect.Lists;
import com.chang.until.reflect.BeanPropertySetter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;

public class ExcelUtils<T> {
   public void setData(ExcelWriter writer, List<T> datas, List<String> props, List<String> titles) {
      for(int i = 0; i < props.size(); ++i) {
         writer.addHeaderAlias((String)props.get(i), (String)titles.get(i));
      }

      List<Object> list = Lists.newArrayList();

      try {
         if (CollectionUtils.isNotEmpty(datas)) {
            Iterator var6 = datas.iterator();

            while(var6.hasNext()) {
               T t = (T) var6.next();
               JSONObject obj = new JSONObject();

               for(int i = 0; i < props.size(); ++i) {
                  BeanPropertySetter beanPropertySetter = new BeanPropertySetter(t, (String)props.get(i));
                  Object result = beanPropertySetter.getValue();
                  obj.put((String)props.get(i), result);
               }

               list.add(obj);
            }
         }
      } catch (Exception var12) {
         var12.printStackTrace();
      }

      writer.write(list, true);
   }

   public void setData(ExcelWriter writer, List<T> datas) {
      List<Object> list = Lists.newArrayList();

      try {
         if (CollectionUtils.isNotEmpty(datas)) {
            T t = datas.get(0);
            Class classType = t.getClass();
            Annotation excelModel = classType.getAnnotation(ExcelModel.class);
            if (excelModel == null) {
               throw new RuntimeException("该实体未配置");
            }

            List<Field> fieldList = (List)Arrays.asList(classType.getDeclaredFields()).stream().filter((item) -> {
               return item.getAnnotation(ExcelColumn.class) != null;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(fieldList)) {
               fieldList.sort((o1, o2) -> {
                  return ((ExcelColumn)o1.getAnnotation(ExcelColumn.class)).sort().compareTo(((ExcelColumn)o2.getAnnotation(ExcelColumn.class)).sort());
               });
               Iterator var8 = fieldList.iterator();

               while(var8.hasNext()) {
                  Field field = (Field)var8.next();
                  writer.addHeaderAlias(field.getName(), ((ExcelColumn)field.getAnnotation(ExcelColumn.class)).value());
               }

               if (CollectionUtils.isNotEmpty(datas)) {
                  var8 = datas.iterator();

                  while(var8.hasNext()) {
                     T data = (T) var8.next();
                     JSONObject obj = new JSONObject();

                     for(int i = 0; i < fieldList.size(); ++i) {
                        String name = ((Field)fieldList.get(i)).getName();
                        BeanPropertySetter beanPropertySetter = new BeanPropertySetter(data, name);
                        Object result = beanPropertySetter.getValue();
                        obj.put(name, result);
                     }

                     list.add(obj);
                  }
               }
            }
         }
      } catch (Exception var15) {
         var15.printStackTrace();
      }

      writer.write(list, true);
   }

   public void export(HttpServletResponse response, List<T> list, String filename) {
      ExcelWriter writer = ExcelUtil.getWriter();
      ServletOutputStream out = null;

      try {
         this.setData(writer, list);
         response.setContentType("application/vnd.ms-excel;charset=utf-8");
         response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
         out = response.getOutputStream();
         writer.flush(out, true);
      } catch (IOException var10) {
         var10.printStackTrace();
         throw new RuntimeException("导出失败");
      } finally {
         writer.close();
      }

   }

   public <T> List<T> importExcel(ExcelReader reader, Class<T> clazz) {
      try {
         ExcelModel excelModel = (ExcelModel)clazz.getAnnotation(ExcelModel.class);
         if (excelModel == null) {
            throw new RuntimeException("该实体未配置");
         } else {
            Field[] declaredFields = clazz.getDeclaredFields();
            Field[] var5 = declaredFields;
            int var6 = declaredFields.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Field field = var5[var7];
               ExcelColumn annotation = (ExcelColumn)field.getAnnotation(ExcelColumn.class);
               String fieldName = field.getName();
               reader.addHeaderAlias(annotation.value(), fieldName);
            }

            List var14 = reader.readAll(clazz);
            return var14;
         }
      } finally {
         reader.close();
      }
   }
}
