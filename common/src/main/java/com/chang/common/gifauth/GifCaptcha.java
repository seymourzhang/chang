package com.chang.common.gifauth;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

public class GifCaptcha extends Captcha {
   public GifCaptcha() {
   }

   public GifCaptcha(int width, int height) {
      this.width = width;
      this.height = height;
   }

   public GifCaptcha(int width, int height, int len) {
      this(width, height);
      this.len = len;
   }

   public GifCaptcha(int width, int height, int len, Font font) {
      this(width, height, len);
      this.font = font;
   }

   public void out(OutputStream os, char[] rands) {
      try {
         GifEncoder gifEncoder = new GifEncoder();
         gifEncoder.start(os);
         gifEncoder.setQuality(180);
         gifEncoder.setDelay(100);
         gifEncoder.setRepeat(0);
         Color[] fontcolor = new Color[this.len];
         Font[] fonts = new Font[this.len];

         int i;
         for(i = 0; i < this.len; ++i) {
            fontcolor[i] = new Color(20 + Randoms.num(110), 20 + Randoms.num(110), 20 + Randoms.num(110));
         }

         for(i = 0; i < this.len; ++i) {
            fonts[i] = new Font("Times New Roman", 3, Randoms.num(10, 30));
         }

         for(i = 0; i < this.len; ++i) {
            BufferedImage frame = this.graphicsImage(fontcolor, rands, i, fonts);
            gifEncoder.addFrame(frame);
            frame.flush();
         }

         gifEncoder.finish();
      } finally {
         Streams.close(os);
      }
   }

   private BufferedImage graphicsImage(Color[] fontcolor, char[] strs, int flag, Font[] fonts) {
      BufferedImage image = new BufferedImage(this.width, this.height, 1);
      Graphics2D g2d = (Graphics2D)image.getGraphics();
      g2d.setColor(Color.WHITE);
      g2d.fillRect(0, 0, this.width, this.height);
      int w = this.width / this.len;

      for(int i = 0; i < this.len; ++i) {
         int h = this.height - (this.height - fonts[i].getSize() >> 1);
         g2d.setFont(fonts[i]);
         AlphaComposite ac3 = AlphaComposite.getInstance(3, this.getAlpha(flag, i));
         g2d.setComposite(ac3);
         g2d.setColor(fontcolor[i]);
         g2d.drawOval(Randoms.num(this.width), Randoms.num(this.height), 5 + Randoms.num(10), 5 + Randoms.num(10));
         this.drowLine(g2d);
         g2d.drawString(strs[i] + "", this.width - (this.len - i) * w + (w - fonts[i].getSize()) + 1, h - 4);
      }

      g2d.dispose();
      return image;
   }

   private void drowLine(Graphics g) {
      int x = Randoms.num(this.width);
      int y = Randoms.num(this.height);
      int xl = Randoms.num(this.width);
      int yl = Randoms.num(this.height);
      g.drawLine(x, y, x + xl, y + yl);
   }

   private float getAlpha(int i, int j) {
      int num = i + j;
      float r = 1.0F / (float)this.len;
      float s = (float)(this.len + 1) * r;
      return num > this.len ? (float)num * r - s : (float)num * r;
   }
}
