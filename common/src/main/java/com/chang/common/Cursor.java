package com.chang.common;

public class Cursor {
   private Integer start;
   private Integer end;

   Cursor(Integer start, Integer end) {
      this.start = start;
      this.end = end;
   }

   public static CursorBuilder builder() {
      return new CursorBuilder();
   }

   public Integer getStart() {
      return this.start;
   }

   public Integer getEnd() {
      return this.end;
   }

   public void setStart(Integer start) {
      this.start = start;
   }

   public void setEnd(Integer end) {
      this.end = end;
   }

   public static class CursorBuilder {
      private Integer start;
      private Integer end;

      CursorBuilder() {
      }

      public CursorBuilder start(Integer start) {
         this.start = start;
         return this;
      }

      public CursorBuilder end(Integer end) {
         this.end = end;
         return this;
      }

      public Cursor build() {
         return new Cursor(this.start, this.end);
      }

      public String toString() {
         return "Cursor.CursorBuilder(start=" + this.start + ", end=" + this.end + ")";
      }
   }
}
