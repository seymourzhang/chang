package com.chang.fileutil.fileQueue;

public class FileIndex {
   private int startIndex;
   private int lens;

   public int getStartIndex() {
      return this.startIndex;
   }

   public int getLens() {
      return this.lens;
   }

   public void setStartIndex(int startIndex) {
      this.startIndex = startIndex;
   }

   public void setLens(int lens) {
      this.lens = lens;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof FileIndex)) {
         return false;
      } else {
         FileIndex other = (FileIndex)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getStartIndex() != other.getStartIndex()) {
            return false;
         } else {
            return this.getLens() == other.getLens();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof FileIndex;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      result = result * 59 + this.getStartIndex();
      result = result * 59 + this.getLens();
      return result;
   }

   public String toString() {
      return "FileIndex(startIndex=" + this.getStartIndex() + ", lens=" + this.getLens() + ")";
   }
}
