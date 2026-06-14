package com.chang.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;

public class AuthCodeUtil {
   private int width = 80;
   private int height = 26;
   private int lineSize = 40;
   private Random random = new Random();
   private String autoCode = "";

   public AuthCodeUtil(String autoCode) {
      this.autoCode = autoCode;
   }

   private Font getFont() {
      return new Font("Fixedsys", 1, 38);
   }

   private Color getRandColor(int fc, int bc) {
      if (fc > 255) {
         fc = 255;
      }

      if (bc > 255) {
         bc = 255;
      }

      int r = fc + this.random.nextInt(bc - fc - 16);
      int g = fc + this.random.nextInt(bc - fc - 14);
      int b = fc + this.random.nextInt(bc - fc - 18);
      return new Color(r, g, b);
   }

   public void createImageStream(HttpServletResponse response) {
      BufferedImage image = new BufferedImage(this.width, this.height, 4);
      Graphics g = image.getGraphics();
      g.fillRect(0, 0, this.width, this.height);
      g.setFont(new Font("Times New Roman", 0, 18));
      g.setColor(this.getRandColor(110, 133));

      for(int i = 0; i <= this.lineSize; ++i) {
         this.drowLine(g);
      }

      this.drowString(g);
      g.dispose();

      try {
         ImageIO.write(image, "JPEG", response.getOutputStream());
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void drowString(Graphics g) {
      g.setFont(this.getFont());

      for(int i = 0; i < this.autoCode.length(); ++i) {
         g.setColor(new Color(this.random.nextInt(101), this.random.nextInt(111), this.random.nextInt(121)));
         g.translate(this.random.nextInt(3), this.random.nextInt(3));
         g.drawString(this.autoCode.charAt(i) + "", 13 * (i + 1), 16);
      }

   }

   private void drowLine(Graphics g) {
      int x = this.random.nextInt(this.width);
      int y = this.random.nextInt(this.height);
      int xl = this.random.nextInt(13);
      int yl = this.random.nextInt(15);
      g.drawLine(x, y, x + xl, y + yl);
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public void setLineSize(int lineSize) {
      this.lineSize = lineSize;
   }

   public static String createAuthCode(int size) {
      String randString = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
      String code = "";

      for(int i = 0; i < size; ++i) {
         code = code + randString.charAt((int)(Math.random() * (double)randString.length()));
      }

      return code;
   }

   public static String createMessageAuthCode() {
      String randString = "123456789";
      String code = "";

      for(int i = 0; i < 6; ++i) {
         code = code + randString.charAt((int)(Math.random() * (double)randString.length()));
      }

      return code;
   }
}
