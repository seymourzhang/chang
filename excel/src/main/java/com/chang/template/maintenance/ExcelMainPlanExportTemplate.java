package com.chang.template.maintenance;

import cn.hutool.core.util.NumberUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.chang.util.ExcelWriterExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelMainPlanExportTemplate {
   private static final Logger log = LoggerFactory.getLogger(ExcelMainPlanExportTemplate.class);

   public static void mainPlanExport(ExcelWriterExtend writer, List<MainPlanTemplate> mainPlanTemplateList) {
      Map<String, Integer> map = new HashMap();
      List<String> head = new ArrayList();
      head.add("电梯名称");
      head.add("维保单位");
      head.add("使用单位");
      head.add("保养类型");
      writer.writeHeadRow(head);
      map.put("电梯名称", 0);
      map.put("维保单位", 1);
      map.put("使用单位", 2);
      map.put("保养类型", 3);
      map.put("计划时间", 4);
      writer.fallBackCurrentRow();
      Integer max = (Integer)mainPlanTemplateList.parallelStream().map((x) -> {
         return NumberUtil.max(new int[]{x.getAnnualMaintenance().size(), x.getHalfMonthMaintenance().size(), x.getHalfYearMaintenance().size(), x.getQuarterlyMaintenance().size()});
      }).max(Integer::max).get();
      if (max == 1) {
         writer.writeHeadRow((Integer)map.get("计划时间"), "计划时间");
      } else {
         writer.mergeColumn(max, (Integer)map.get("计划时间"), "计划时间", true);
      }

      writer.passCurrentRow();
      Iterator var5 = mainPlanTemplateList.iterator();

      while(var5.hasNext()) {
         MainPlanTemplate mainPlanTemplate = (MainPlanTemplate)var5.next();
         writer.mergeRow(4, (Integer)map.get("电梯名称"), mainPlanTemplate.getElevatorName());
         writer.mergeRow(4, (Integer)map.get("维保单位"), mainPlanTemplate.getMaintenanceUnit());
         writer.mergeRow(4, (Integer)map.get("使用单位"), mainPlanTemplate.getUserUnit());
         Map<String, List<String>> planMap = new LinkedHashMap();
         planMap.put("半月保", mainPlanTemplate.getHalfMonthMaintenance());
         planMap.put("季度保", mainPlanTemplate.getQuarterlyMaintenance());
         planMap.put("半年保", mainPlanTemplate.getHalfYearMaintenance());
         planMap.put("年度保", mainPlanTemplate.getAnnualMaintenance());
         writer.writerColumnMap((Integer)map.get("保养类型"), planMap);
      }

   }

   public static void export(HttpServletResponse response, List<MainPlanTemplate> templates, String filename) {
      ExcelWriterExtend writer = ExcelWriterExtend.getWriter();
      ServletOutputStream out = null;

      try {
         mainPlanExport(writer, templates);
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
