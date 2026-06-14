package com.chang.common.io;

import java.io.IOException;
import java.io.Reader;

public class UnsafeStringReader extends Reader {
   private String mString;
   private int mPosition;
   private int mLimit;
   private int mMark;

   public UnsafeStringReader(String str) {
      this.mString = str;
      this.mLimit = str.length();
      this.mPosition = this.mMark = 0;
   }

   public int read() throws IOException {
      this.ensureOpen();
      return this.mPosition >= this.mLimit ? -1 : this.mString.charAt(this.mPosition++);
   }

   public int read(char[] cs, int off, int len) throws IOException {
      this.ensureOpen();
      if (off >= 0 && off <= cs.length && len >= 0 && off + len <= cs.length && off + len >= 0) {
         if (len == 0) {
            return 0;
         } else if (this.mPosition >= this.mLimit) {
            return -1;
         } else {
            int n = Math.min(this.mLimit - this.mPosition, len);
            this.mString.getChars(this.mPosition, this.mPosition + n, cs, off);
            this.mPosition += n;
            return n;
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public long skip(long ns) throws IOException {
      this.ensureOpen();
      if (this.mPosition >= this.mLimit) {
         return 0L;
      } else {
         long n = Math.min((long)(this.mLimit - this.mPosition), ns);
         n = Math.max((long)(-this.mPosition), n);
         this.mPosition = (int)((long)this.mPosition + n);
         return n;
      }
   }

   public boolean ready() throws IOException {
      this.ensureOpen();
      return true;
   }

   public boolean markSupported() {
      return true;
   }

   public void mark(int readAheadLimit) throws IOException {
      if (readAheadLimit < 0) {
         throw new IllegalArgumentException("Read-ahead limit < 0");
      } else {
         this.ensureOpen();
         this.mMark = this.mPosition;
      }
   }

   public void reset() throws IOException {
      this.ensureOpen();
      this.mPosition = this.mMark;
   }

   public void close() throws IOException {
      this.mString = null;
   }

   private void ensureOpen() throws IOException {
      if (this.mString == null) {
         throw new IOException("Stream closed");
      }
   }
}
