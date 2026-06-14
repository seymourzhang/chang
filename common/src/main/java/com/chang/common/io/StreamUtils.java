package com.chang.common.io;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
   private StreamUtils() {
   }

   public static InputStream limitedInputStream(final InputStream is, final int limit) throws IOException {
      return new InputStream() {
         private int mPosition = 0;
         private int mMark = 0;
         private int mLimit = Math.min(limit, is.available());

         public int read() throws IOException {
            if (this.mPosition < this.mLimit) {
               ++this.mPosition;
               return is.read();
            } else {
               return -1;
            }
         }

         public int read(byte[] b, int off, int len) throws IOException {
            if (b == null) {
               throw new NullPointerException();
            } else if (off >= 0 && len >= 0 && len <= b.length - off) {
               if (this.mPosition >= this.mLimit) {
                  return -1;
               } else {
                  if (this.mPosition + len > this.mLimit) {
                     len = this.mLimit - this.mPosition;
                  }

                  if (len <= 0) {
                     return 0;
                  } else {
                     is.read(b, off, len);
                     this.mPosition += len;
                     return len;
                  }
               }
            } else {
               throw new IndexOutOfBoundsException();
            }
         }

         public long skip(long len) throws IOException {
            if ((long)this.mPosition + len > (long)this.mLimit) {
               len = (long)(this.mLimit - this.mPosition);
            }

            if (len <= 0L) {
               return 0L;
            } else {
               is.skip(len);
               this.mPosition = (int)((long)this.mPosition + len);
               return len;
            }
         }

         public int available() {
            return this.mLimit - this.mPosition;
         }

         public boolean markSupported() {
            return is.markSupported();
         }

         public void mark(int readlimit) {
            is.mark(readlimit);
            this.mMark = this.mPosition;
         }

         public void reset() throws IOException {
            is.reset();
            this.mPosition = this.mMark;
         }

         public void close() throws IOException {
            is.close();
         }
      };
   }

   public static InputStream markSupportedInputStream(final InputStream is, final int markBufferSize) {
      return is.markSupported() ? is : new InputStream() {
         byte[] mMarkBuffer;
         boolean mInMarked = false;
         boolean mInReset = false;
         boolean mDry = false;
         private int mPosition = 0;
         private int mCount = 0;

         public int read() throws IOException {
            if (!this.mInMarked) {
               return is.read();
            } else if (this.mPosition < this.mCount) {
               byte b = this.mMarkBuffer[this.mPosition++];
               return b & 255;
            } else if (!this.mInReset) {
               if (this.mDry) {
                  return -1;
               } else {
                  if (null == this.mMarkBuffer) {
                     this.mMarkBuffer = new byte[markBufferSize];
                  }

                  if (this.mPosition >= markBufferSize) {
                     throw new IOException("Mark buffer is full!");
                  } else {
                     int read = is.read();
                     if (-1 == read) {
                        this.mDry = true;
                        return -1;
                     } else {
                        this.mMarkBuffer[this.mPosition++] = (byte)read;
                        ++this.mCount;
                        return read;
                     }
                  }
               }
            } else {
               this.mInMarked = false;
               this.mInReset = false;
               this.mPosition = 0;
               this.mCount = 0;
               return is.read();
            }
         }

         public synchronized void mark(int readlimit) {
            this.mInMarked = true;
            this.mInReset = false;
            int count = this.mCount - this.mPosition;
            if (count > 0) {
               System.arraycopy(this.mMarkBuffer, this.mPosition, this.mMarkBuffer, 0, count);
               this.mCount = count;
               this.mPosition = 0;
            }

         }

         public synchronized void reset() throws IOException {
            if (!this.mInMarked) {
               throw new IOException("should mark before reset!");
            } else {
               this.mInReset = true;
               this.mPosition = 0;
            }
         }

         public boolean markSupported() {
            return true;
         }

         public int available() throws IOException {
            int available = is.available();
            if (this.mInMarked && this.mInReset) {
               available += this.mCount - this.mPosition;
            }

            return available;
         }

         public void close() throws IOException {
            is.close();
         }
      };
   }

   public static InputStream markSupportedInputStream(InputStream is) {
      return markSupportedInputStream(is, 1024);
   }

   public static void skipUnusedStream(InputStream is) throws IOException {
      if (is.available() > 0) {
         is.skip((long)is.available());
      }

   }
}
