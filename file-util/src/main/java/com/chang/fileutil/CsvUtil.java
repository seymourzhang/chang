package com.chang.fileutil;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvUtil {
   public static long getCsvFileLineSize(String fileName, char split, boolean hasHeaders) throws Exception {
      CsvReader csvReader = new CsvReader(fileName, split, Charset.forName("UTF-8"));
      if (hasHeaders) {
         csvReader.readHeaders();
      }

      long num;
      for(num = 0L; csvReader.readRecord(); num = csvReader.getCurrentRecord()) {
      }

      return num;
   }

   public static List<String[]> ReadCsvLineRange(String fileName, char split, Long startLine, Long endLine, boolean hasHeaders) throws Exception {
      CsvReader csvReader = new CsvReader(fileName, split, Charset.forName("UTF-8"));
      if (hasHeaders) {
         csvReader.readHeaders();
      }

      List<String[]> data = new ArrayList();

      while(true) {
         do {
            do {
               if (!csvReader.readRecord()) {
                  csvReader.close();
                  return data;
               }
            } while(csvReader.getCurrentRecord() < startLine);
         } while(csvReader.getCurrentRecord() >= endLine);

         int lens = csvReader.getColumnCount();
         List<String> subdata = new ArrayList();

         for(int i = 0; i < lens; ++i) {
            subdata.add(csvReader.get(i));
         }

         data.add(subdata.toArray(new String[data.size()]));
      }
   }

   public static List<String[]> ReadCsvAllData(String fileName, char split, boolean hasHeaders) throws IOException {
      CsvReader csvReader = new CsvReader(fileName, split, Charset.forName("UTF-8"));
      if (hasHeaders) {
         csvReader.readHeaders();
      }

      csvReader.setSafetySwitch(false);
      List<String[]> data = new ArrayList();

      while(csvReader.readRecord()) {
         int lens = csvReader.getColumnCount();
         List<String> subdata = new ArrayList();

         for(int i = 0; i < lens; ++i) {
            subdata.add(csvReader.get(i));
         }

         data.add(subdata.toArray(new String[subdata.size()]));
      }

      csvReader.close();
      return data;
   }

   public static CsvWriter getCsvWriter(String fileName, char split) {
      return new CsvWriter(fileName, split, Charset.forName("UTF-8"));
   }

   public static void writeCsv(CsvWriter csvWriter, String[] data) throws IOException {
      csvWriter.writeRecord(data);
   }

   public static void writeCsv(CsvWriter csvWriter, List<String[]> datas) throws IOException {
      Iterator var2 = datas.iterator();

      while(var2.hasNext()) {
         String[] data = (String[])var2.next();
         csvWriter.writeRecord(data);
      }

   }

   public static void close(CsvWriter csvWriter) {
      csvWriter.close();
   }

   public static void writeCsv(String fileName, char split, List<String[]> datas) throws IOException {
      CsvWriter csvWriter = new CsvWriter(fileName, split, Charset.forName("UTF-8"));
      Iterator var4 = datas.iterator();

      while(var4.hasNext()) {
         String[] data = (String[])var4.next();
         csvWriter.writeRecord(data);
      }

      csvWriter.close();
   }
}
