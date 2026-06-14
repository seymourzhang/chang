package com.chang.common.draw;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryTick;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.text.TextBlock;
import org.jfree.ui.RectangleEdge;

public class IntervalCategoryAxis extends CategoryAxis {
   private final int stepNum;

   public IntervalCategoryAxis(int stepNum) {
      this.stepNum = stepNum;
   }

   public List<Tick> refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {
      List<Tick> ticks = new ArrayList();
      if (!(dataArea.getHeight() <= 0.0) && !(dataArea.getWidth() < 0.0)) {
         CategoryPlot plot = (CategoryPlot)this.getPlot();
         List<?> categories = plot.getCategoriesForAxis(this);
         double max = 0.0;
         if (categories != null) {
            CategoryLabelPosition position = super.getCategoryLabelPositions().getLabelPosition(edge);
            int categoryIndex = 0;

            for(Iterator var12 = categories.iterator(); var12.hasNext(); ++categoryIndex) {
               Object o = var12.next();
               Comparable<?> category = (Comparable)o;
               g2.setFont(this.getTickLabelFont(category));
               TextBlock label = new TextBlock();
               label.addLine(category.toString(), this.getTickLabelFont(category), this.getTickLabelPaint(category));
               if (edge != RectangleEdge.TOP && edge != RectangleEdge.BOTTOM) {
                  if (edge == RectangleEdge.LEFT || edge == RectangleEdge.RIGHT) {
                     max = Math.max(max, this.calculateTextBlockWidth(label, position, g2));
                  }
               } else {
                  max = Math.max(max, this.calculateTextBlockHeight(label, position, g2));
               }

               if (categoryIndex % this.stepNum == 0) {
                  Tick tick = new CategoryTick(category, label, position.getLabelAnchor(), position.getRotationAnchor(), position.getAngle());
                  ticks.add(tick);
               }
            }
         }

         state.setMax(max);
         return ticks;
      } else {
         return ticks;
      }
   }
}
