package com.chang.common.gifauth;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;

public abstract class Captcha {
   protected Font font = new Font("Verdana", 3, 28);
   protected int len = 5;
   protected int width = 150;
   protected int height = 40;
   private String chars = null;

   public char[] alphas() {
      char[] cs = new char[this.len];

      for(int i = 0; i < this.len; ++i) {
         cs[i] = Randoms.alpha();
      }

      this.chars = new String(cs);
      return cs;
   }

   public Font getFont() {
      return this.font;
   }

   public void setFont(Font font) {
      this.font = font;
   }

   public int getLen() {
      return this.len;
   }

   public void setLen(int len) {
      this.len = len;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   protected Color color(int fc, int bc) {
      if (fc > 255) {
         fc = 255;
      }

      if (bc > 255) {
         bc = 255;
      }

      int r = fc + Randoms.num(bc - fc);
      int g = fc + Randoms.num(bc - fc);
      int b = fc + Randoms.num(bc - fc);
      return new Color(r, g, b);
   }

   public abstract void out(OutputStream var1, char[] var2);

   public String text() {
      return this.chars;
   }
}
