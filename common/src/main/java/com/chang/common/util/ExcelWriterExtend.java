package com.chang.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.DependencyException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.cell.CellUtil;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelWriterExtend extends ExcelWriter {
   private AtomicInteger currentColumn = new AtomicInteger(0);

   public ExcelWriterExtend(String destFilePath) {
      super(destFilePath);
   }

   public ExcelWriterExtend() {
   }

   public ExcelWriterExtend(boolean isXlsx) {
      super(isXlsx);
   }

   public ExcelWriterExtend(boolean isXlsx, String sheetName) {
      super(isXlsx, sheetName);
   }

   public ExcelWriterExtend(String destFilePath, String sheetName) {
      super(destFilePath, sheetName);
   }

   public ExcelWriterExtend(File destFile) {
      super(destFile);
   }

   public ExcelWriterExtend(File destFile, String sheetName) {
      super(destFile, sheetName);
   }

   public ExcelWriterExtend(Workbook workbook, String sheetName) {
      super(workbook, sheetName);
   }

   public ExcelWriterExtend(Sheet sheet) {
      super(sheet);
   }

   public static ExcelWriterExtend getWriter(String destFilePath) {
      try {
         return new ExcelWriterExtend(destFilePath);
      } catch (NoClassDefFoundError var2) {
         throw new DependencyException((Throwable)ObjectUtil.defaultIfNull(var2.getCause(), var2), "You need to add dependency of 'poi-ooxml' to your project, and version >= 4.1.2", new Object[0]);
      }
   }

   public static ExcelWriterExtend getWriter() {
      try {
         return new ExcelWriterExtend();
      } catch (NoClassDefFoundError var1) {
         throw new DependencyException((Throwable)ObjectUtil.defaultIfNull(var1.getCause(), var1), "You need to add dependency of 'poi-ooxml' to your project, and version >= 4.1.2", new Object[0]);
      }
   }

   public ExcelWriterExtend mergeRow(int lastRow, int currentColumn) {
      this.mergeRow(lastRow, currentColumn, (Object)null, true);
      return this;
   }

   public ExcelWriterExtend mergeRow(int lastRow, int currentColumn, Object content) {
      this.mergeRow(lastRow, currentColumn, content, false);
      return this;
   }

   public ExcelWriterExtend mergeRow(int mergeNum, int currentColumn, Object content, boolean isSetHeaderStyle) {
      Assert.isFalse(this.isClosed, "ExcelWriter has been closed!", new Object[0]);
      int currentRow = this.getCurrentRow();
      this.merge(currentRow, currentRow + mergeNum - 1, currentColumn, currentColumn, content, isSetHeaderStyle);
      return this;
   }

   public ExcelWriterExtend mergeColumn(int mergeNum, int currentColumn, Object content, boolean isSetHeaderStyle) {
      Assert.isFalse(this.isClosed, "ExcelWriter has been closed!", new Object[0]);
      int currentRow = this.getCurrentRow();
      this.merge(currentRow, currentRow, currentColumn, currentColumn + mergeNum - 1, content, isSetHeaderStyle);
      return this;
   }

   public ExcelWriterExtend writeHeadRow(int column, Object value) {
      Assert.isFalse(this.isClosed, "ExcelWriter has been closed!", new Object[0]);
      Cell cell = this.getOrCreateCell(column, this.getCurrentRow());
      CellUtil.setCellValue(cell, value, this.getStyleSet(), true);
      return this;
   }

   public ExcelWriterExtend writeRow(int column, Object value) {
      Assert.isFalse(this.isClosed, "ExcelWriter has been closed!", new Object[0]);
      Cell cell = this.getOrCreateCell(column, this.getCurrentRow());
      CellUtil.setCellValue(cell, value, this.getStyleSet(), false);
      this.passCurrentRow();
      return this;
   }

   public ExcelWriterExtend writeRowNotPass(int column, Object value) {
      Assert.isFalse(this.isClosed, "ExcelWriter has been closed!", new Object[0]);
      Cell cell = this.getOrCreateCell(column, this.getCurrentRow());
      CellUtil.setCellValue(cell, value, this.getStyleSet(), false);
      return this;
   }

   public ExcelWriterExtend writerRowList(final int column, Iterable<?> rowData) {
      Iterator var3 = rowData.iterator();

      while(var3.hasNext()) {
         Object data = var3.next();
         int currentRow = this.getCurrentRow();
         this.writeCellValue(column, currentRow, data);
         this.passCurrentRow();
      }

      return this;
   }

   public ExcelWriterExtend writerColumn(final int column, Iterable<?> rowData) {
      int currentRow = this.getCurrentRow();
      this.currentColumn.set(column);
      Iterator var4 = rowData.iterator();

      while(var4.hasNext()) {
         Object data = var4.next();
         this.writeCellValue(this.currentColumn.getAndIncrement(), currentRow, data);
      }

      return this;
   }

   public ExcelWriterExtend writerColumnValue(final int column, Object data) {
      int currentRow = this.getCurrentRow();
      this.writeCellValue(column, currentRow, data);
      return this;
   }

   public ExcelWriterExtend fallBackCurrentRow() {
      int currentRow = this.getCurrentRow();
      this.setCurrentRow(currentRow - 1);
      return this;
   }

   public <T> ExcelWriterExtend writerColumnMap(final int columnPosition, Map<String, List<T>> map) {
      Iterator var3 = map.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, List<T>> entry = (Map.Entry)var3.next();
         String key = (String)entry.getKey();
         List<?> value = (List)entry.getValue();
         this.writerColumnValue(columnPosition, key);
         this.writerColumn(columnPosition + 1, value);
         this.passCurrentRow();
      }

      return this;
   }

   public <T> ExcelWriterExtend writerMap(final int columnPosition, Map<String, List<T>> map) {
      Iterator var3 = map.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, List<T>> entry = (Map.Entry)var3.next();
         String key = (String)entry.getKey();
         List<?> value = (List)entry.getValue();
         int size = value.size();
         if (size >= 2) {
            this.mergeRow(size, columnPosition, key);
         } else {
            this.writeRow(columnPosition, key);
            this.fallBackCurrentRow();
         }

         for(Iterator var8 = value.iterator(); var8.hasNext(); this.passCurrentRow()) {
            Object o = var8.next();
            if (o instanceof Map) {
               this.writerColumn(columnPosition + 1, ((Map)o).values());
            } else if (BeanUtil.isBean(o.getClass())) {
               Map rowMap = BeanUtil.beanToMap(o, new LinkedHashMap(), false, false);
               this.writerColumn(columnPosition + 1, rowMap.values());
            } else {
               this.writerColumn(columnPosition + 1, CollUtil.newArrayList(new Object[]{o}));
            }
         }
      }

      return this;
   }

   public <T> ExcelWriterExtend writerMapMap(final int columnPosition, Map<String, Map<String, List<T>>> map) {
      Iterator var3 = map.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, Map<String, List<T>>> entry = (Map.Entry)var3.next();
         String key = (String)entry.getKey();
         Map<String, List<T>> value = (Map)entry.getValue();
         Integer rowNum = (Integer)value.values().stream().map((x) -> {
            return x.size();
         }).reduce(Integer::sum).get();
         if (rowNum == 1) {
            this.writeRow(columnPosition, key);
            this.fallBackCurrentRow();
         } else {
            this.mergeRow(rowNum, columnPosition, key);
         }

         Iterator var8 = value.entrySet().iterator();

         while(var8.hasNext()) {
            Map.Entry<String, List<T>> subEntry = (Map.Entry)var8.next();
            String subKey = (String)subEntry.getKey();
            List<?> subValue = (List)subEntry.getValue();
            int size = subValue.size();
            if (size >= 2) {
               this.mergeRow(size, columnPosition + 1, subKey);
            } else {
               this.writeRow(columnPosition + 1, subKey);
               this.fallBackCurrentRow();
            }

            for(Iterator var13 = subValue.iterator(); var13.hasNext(); this.passCurrentRow()) {
               Object o = var13.next();
               if (o instanceof Map) {
                  this.writerColumn(columnPosition + 2, ((Map)o).values());
               } else if (BeanUtil.isBean(o.getClass())) {
                  Map rowMap = BeanUtil.beanToMap(o, new LinkedHashMap(), false, false);
                  this.writerColumn(columnPosition + 2, rowMap.values());
               } else {
                  this.writerColumn(columnPosition + 2, CollUtil.newArrayList(new Object[]{o}));
               }
            }
         }
      }

      return this;
   }
}
