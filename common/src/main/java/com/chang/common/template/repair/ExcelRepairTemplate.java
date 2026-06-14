package com.chang.common.template.repair;

import com.chang.common.util.ExcelWriterExtend;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ExcelRepairTemplate {
   public static void repairExport(ExcelWriterExtend writer, List<RepairTemplate> repairTemplateList) {
      Map<String, Integer> map = new HashMap();
      map.put("维修模版名称", 0);
      map.put("电梯类型", 1);
      map.put("创建人", 2);
      map.put("维修部位1", 3);
      map.put("维修部位2", 4);
      map.put("维修点", 5);
      writer.writeHeadRow((Integer)map.get("维修模版名称"), "维修模版名称");
      writer.writeHeadRow((Integer)map.get("电梯类型"), "电梯类型");
      writer.writeHeadRow((Integer)map.get("创建人"), "创建人");
      writer.mergeColumn(2, (Integer)map.get("维修部位1"), "维修部位", true);
      writer.writeHeadRow((Integer)map.get("维修点"), "维修点");
      writer.passCurrentRow();
      Iterator var3 = repairTemplateList.iterator();

      while(var3.hasNext()) {
         RepairTemplate repairTemplate = (RepairTemplate)var3.next();
         Map<String, Map<String, List<String>>> repair = repairTemplate.getRepair();
         Integer mergeNum = (Integer)repair.values().stream().map((x) -> {
            return x.values();
         }).map((y) -> {
            return (Integer)y.stream().map((yy) -> {
               return yy.size();
            }).reduce(Integer::sum).get();
         }).reduce(Integer::sum).get();
         writer.mergeRow(mergeNum, (Integer)map.get("维修模版名称"), repairTemplate.getRepairTemplateName());
         writer.mergeRow(mergeNum, (Integer)map.get("电梯类型"), repairTemplate.getElevatorType());
         writer.mergeRow(mergeNum, (Integer)map.get("创建人"), repairTemplate.getCreator());
         writer.writerMapMap((Integer)map.get("维修部位1"), repair);
      }

   }

   public static void export(HttpServletResponse response, List<RepairTemplate> templates, String filename) {
      ExcelWriterExtend writer = ExcelWriterExtend.getWriter();
      ServletOutputStream out = null;

      try {
         repairExport(writer, templates);
         response.setContentType("application/vnd.ms-excel;charset=utf-8");
         response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
         out = response.getOutputStream();
         writer.flush(out, true);
      } catch (IOException var9) {
         var9.printStackTrace();
         throw new RuntimeException("导出失败");
      } finally {
         writer.close();
      }

   }
}
