package com.chang.common.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class LineChartUtils {
   private static String NO_DATA_MSG = "数据加载失败";
   private static Font FONT = new Font("宋体", 0, 12);
   private static LineChartUtils lineChartUtils = new LineChartUtils();
   private static final ThreadLocal<Integer> size = new ThreadLocal();
   public static Color[] CHART_COLORS = new Color[]{new Color(31, 129, 188), new Color(92, 92, 97), new Color(144, 237, 125), new Color(255, 188, 117), new Color(153, 158, 255), new Color(255, 117, 153), new Color(253, 236, 109), new Color(128, 133, 232), new Color(158, 90, 102), new Color(255, 204, 102)};

   public LineChartUtils() {
      this.setChartTheme();
   }

   public static LineChartUtils getLineChartUtils() {
      return lineChartUtils;
   }

   private void setChartTheme() {
      StandardChartTheme chartTheme = new StandardChartTheme("CN");
      chartTheme.setExtraLargeFont(FONT);
      chartTheme.setRegularFont(FONT);
      chartTheme.setLargeFont(FONT);
      chartTheme.setSmallFont(FONT);
      chartTheme.setTitlePaint(new Color(51, 51, 51));
      chartTheme.setSubtitlePaint(new Color(85, 85, 85));
      chartTheme.setLegendBackgroundPaint(Color.WHITE);
      chartTheme.setLegendItemPaint(Color.BLACK);
      chartTheme.setChartBackgroundPaint(Color.WHITE);
      Paint[] OUTLINE_PAINT_SEQUENCE = new Paint[]{Color.WHITE};
      new DefaultDrawingSupplier(CHART_COLORS, CHART_COLORS, OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
      chartTheme.setPlotBackgroundPaint(Color.WHITE);
      chartTheme.setPlotOutlinePaint(Color.WHITE);
      chartTheme.setLabelLinkPaint(new Color(8, 55, 114));
      chartTheme.setLabelLinkStyle(PieLabelLinkStyle.CUBIC_CURVE);
      chartTheme.setAxisOffset(new RectangleInsets(5.0, 12.0, 5.0, 12.0));
      chartTheme.setDomainGridlinePaint(new Color(8, 55, 114));
      chartTheme.setRangeGridlinePaint(new Color(192, 192, 192));
      chartTheme.setBaselinePaint(Color.WHITE);
      chartTheme.setCrosshairPaint(Color.BLUE);
      chartTheme.setAxisLabelPaint(new Color(51, 51, 51));
      chartTheme.setTickLabelPaint(new Color(67, 67, 72));
      chartTheme.setBarPainter(new StandardBarPainter());
      chartTheme.setXYBarPainter(new StandardXYBarPainter());
      chartTheme.setItemLabelPaint(Color.black);
      chartTheme.setThermometerPaint(Color.white);
      ChartFactory.setChartTheme(chartTheme);
   }

   private boolean isPercent(String str) {
      return str != null ? str.endsWith("%") && this.isNumber(str.substring(0, str.length() - 1)) : false;
   }

   private boolean isNumber(String str) {
      return str != null ? str.matches("^[-+]?(([0-9]+)((([.]{0})([0-9]*))|(([.]{1})([0-9]+))))$") : false;
   }

   private DefaultCategoryDataset createDataset(List<String> categories, List<Serie> series) {
      String[] cate = (String[])categories.toArray(new String[categories.size()]);
      return this.createDefaultCategoryDataset(series, cate);
   }

   private void setAntiAlias(JFreeChart chart) {
      chart.setTextAntiAlias(false);
   }

   private void setLineRender(CategoryPlot plot, boolean isShowDataLabels) {
      this.setLineRender(plot, isShowDataLabels, false);
   }

   private void setLineRender(CategoryPlot plot, boolean isShowDataLabels, boolean isShapesVisible) {
      plot.setNoDataMessage(NO_DATA_MSG);
      plot.setInsets(new RectangleInsets(10.0, 10.0, 0.0, 10.0), false);
      LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
      renderer.setStroke(new BasicStroke(1.5F));
      if (isShowDataLabels) {
         renderer.setBaseItemLabelsVisible(true);
         renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", NumberFormat.getInstance()));
         renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BOTTOM_CENTER));
      }

      renderer.setBaseShapesVisible(isShapesVisible);
      this.setXAixs(plot);
      this.setYAixs(plot);
   }

   private void setXAixs(CategoryPlot plot) {
      Integer xSize = (Integer)size.get();
      Integer stepNum = xSize < 20 ? 2 : xSize / 10;
      Color lineColor = new Color(31, 121, 170);
      CategoryAxis axis = new IntervalCategoryAxis(stepNum);
      axis.setAxisLineVisible(false);
      axis.setTickMarksVisible(false);
      axis.setLabelFont(FONT);
      axis.setTickLabelFont(FONT);
      axis.setAxisLinePaint(lineColor);
      axis.setTickMarkPaint(lineColor);
      axis.setUpperMargin(0.0);
      axis.setLowerMargin(0.0);
      plot.setDomainAxis(axis);
   }

   private void setLegendEmptyBorder(JFreeChart chart) {
      chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
   }

   private void setYAixs(CategoryPlot plot) {
      Color lineColor = new Color(192, 208, 224);
      ValueAxis axis = plot.getRangeAxis();
      axis.setAxisLinePaint(lineColor);
      axis.setTickMarkPaint(lineColor);
      axis.setAxisLineVisible(false);
      axis.setTickMarksVisible(false);
      plot.setRangeGridlinePaint(new Color(192, 192, 192));
      plot.setRangeGridlineStroke(new BasicStroke(1.0F));
      plot.getRangeAxis().setUpperMargin(0.1);
      plot.getRangeAxis().setLowerMargin(0.1);
   }

   private ChartPanel createChart(String title, String xTitle, String yTitle, List<String> categories, List<Serie> series) {
      JFreeChart chart = ChartFactory.createLineChart(title, xTitle, yTitle, this.createDataset(categories, series), PlotOrientation.VERTICAL, true, false, false);
      this.setAntiAlias(chart);
      this.setLineRender(chart.getCategoryPlot(), false, true);
      this.setXAixs(chart.getCategoryPlot());
      this.setYAixs(chart.getCategoryPlot());
      chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
      ChartPanel chartPanel = new ChartPanel(chart);
      return chartPanel;
   }

   private void saveAsFile(JFreeChart chart, String outputPath, int weight, int height) throws Exception {
      FileOutputStream out = null;
      File outFile = new File(outputPath);
      if (!outFile.getParentFile().exists()) {
         outFile.getParentFile().mkdirs();
      }

      out = new FileOutputStream(outputPath);
      ChartUtilities.writeChartAsPNG(out, chart, weight, height);
      out.flush();
      if (out != null) {
         try {
            out.close();
         } catch (IOException var8) {
            var8.printStackTrace();
         }
      }

   }

   private DefaultCategoryDataset createDefaultCategoryDataset(List<Serie> series, String[] categories) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      Iterator var4 = series.iterator();

      while(true) {
         String name;
         Vector data;
         do {
            do {
               do {
                  if (!var4.hasNext()) {
                     return dataset;
                  }

                  Serie serie = (Serie)var4.next();
                  name = serie.getName();
                  data = serie.getData();
               } while(data == null);
            } while(categories == null);
         } while(data.size() != categories.length);

         for(int index = 0; index < data.size(); ++index) {
            String value = data.get(index) == null ? "" : data.get(index).toString();
            if (this.isPercent(value)) {
               value = value.substring(0, value.length() - 1);
            }

            if (this.isNumber(value)) {
               dataset.setValue(Double.parseDouble(value), name, categories[index]);
            }
         }
      }
   }

   public void CreateNewLineChartForPngBackFile(String title, String xTitle, String yTitle, String filepath, List<String> categories, List<Serie> series, int width, int height) throws Exception {
      size.set(categories.size());
      ChartPanel chartPanel = this.createChart(title, xTitle, yTitle, categories, series);
      this.saveAsFile(chartPanel.getChart(), filepath, width, height);
   }

   public void CreateNewLineChartForPngBackOutputStream(String title, String xTitle, String yTitle, OutputStream out, List<String> categories, List<Serie> series, int width, int height) throws Exception {
      size.set(categories.size());
      ChartPanel chartPanel = this.createChart(title, xTitle, yTitle, categories, series);
      ChartUtilities.writeChartAsPNG(out, chartPanel.getChart(), width, height);
   }
}
