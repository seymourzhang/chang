package com.chang.common.gifauth;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class SpecCaptcha extends Captcha {
   public SpecCaptcha() {
   }

   public SpecCaptcha(int width, int height) {
      this.width = width;
      this.height = height;
   }

   public SpecCaptcha(int width, int height, int len) {
      this(width, height);
      this.len = len;
   }

   public SpecCaptcha(int width, int height, int len, Font font) {
      this(width, height, len);
      this.font = font;
   }

   public void out(OutputStream out, char[] chars) {
      this.graphicsImage(chars, out);
   }

   private boolean graphicsImage(char[] strs, OutputStream out) {
      boolean ok = false;

      try {
         BufferedImage bi = new BufferedImage(this.width, this.height, 1);
         Graphics2D g = (Graphics2D)bi.getGraphics();
         int len = strs.length;
         g.setColor(Color.WHITE);
         g.fillRect(0, 0, this.width, this.height);

         Color color;
         int h;
         for(h = 0; h < 15; ++h) {
            color = this.color(150, 250);
            g.setColor(color);
            g.drawOval(Randoms.num(this.width), Randoms.num(this.height), 5 + Randoms.num(10), 5 + Randoms.num(10));
            color = null;
         }

         for(h = 0; h < 15; ++h) {
            color = this.color(150, 250);
            g.setColor(color);
            g.drawLine(Randoms.num(this.width), Randoms.num(this.height), 5 + Randoms.num(10), 5 + Randoms.num(10));
            color = null;
         }

         g.setFont(this.font);
         h = this.height - (this.height - this.font.getSize() >> 1);
         int w = this.width / len;
         int size = w - this.font.getSize() + 1;

         for(int i = 0; i < len; ++i) {
            AlphaComposite ac3 = AlphaComposite.getInstance(3, 0.7F);
            g.setComposite(ac3);
            color = new Color(20 + Randoms.num(110), 20 + Randoms.num(110), 20 + Randoms.num(110));
            g.setColor(color);
            g.drawString(strs[i] + "", this.width - (len - i) * w + size, h - 4);
            color = null;
            ac3 = null;
         }

         ImageIO.write(bi, "png", out);
         out.flush();
         ok = true;
      } catch (IOException var16) {
         ok = false;
      } finally {
         Streams.close(out);
      }

      return ok;
   }
}
