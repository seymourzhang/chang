package com.chang.template.maintenance;

import com.chang.util.ExcelWriterExtend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ExcelMaintenanceTemplate {
   public static void maintenanceExport(ExcelWriterExtend writer, List<MaintenanceTemplate> templates) {
      Map<String, Integer> map = new HashMap();
      List<String> head = new ArrayList();
      head.add("保养模板名称");
      head.add("电梯类型");
      head.add("保养类型");
      head.add("创建人");
      head.add("保养部位");
      head.add("保养项目");
      head.add("保养项目要求");
      writer.writeHeadRow(head);
      map.put("保养模板名称", 0);
      map.put("电梯类型", 1);
      map.put("保养类型", 2);
      map.put("创建人", 3);
      map.put("保养部位", 4);
      map.put("保养项目", 5);
      map.put("保养项目要求", 6);

      MaintenanceTemplate template;
      for(Iterator var4 = templates.iterator(); var4.hasNext(); writer.writerMap((Integer)map.get("保养部位"), template.getMaintenanceList())) {
         template = (MaintenanceTemplate)var4.next();
         Map<String, List<Maintenance>> maintenanceList = template.getMaintenanceList();
         int allSize = (Integer)maintenanceList.values().stream().map(List::size).reduce(Integer::sum).get();
         if (allSize >= 2) {
            writer.mergeRow(allSize, (Integer)map.get("保养模板名称"), template.getMaintenanceTemplateName());
            writer.mergeRow(allSize, (Integer)map.get("电梯类型"), template.getElevatorType());
            writer.mergeRow(allSize, (Integer)map.get("保养类型"), template.getMaintenanceType());
            writer.mergeRow(allSize, (Integer)map.get("创建人"), template.getCreator());
         } else {
            writer.writeRowNotPass((Integer)map.get("保养模板名称"), template.getMaintenanceTemplateName());
            writer.writeRowNotPass((Integer)map.get("电梯类型"), template.getElevatorType());
            writer.writeRowNotPass((Integer)map.get("保养类型"), template.getMaintenanceType());
            writer.writeRowNotPass((Integer)map.get("创建人"), template.getCreator());
         }
      }

   }

   public static void export(HttpServletResponse response, List<MaintenanceTemplate> templates, String filename) {
      ExcelWriterExtend writer = ExcelWriterExtend.getWriter();
      ServletOutputStream out = null;

      try {
         maintenanceExport(writer, templates);
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
