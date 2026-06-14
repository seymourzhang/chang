package com.chang.until.httpFileDownLoad;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class FileAccess implements Serializable {
   private static final long serialVersionUID = 2243839061485535798L;
   private String sName;
   private long nPos;
   private RandomAccessFile oSavedFile = null;

   public FileAccess(String sName, long nPos) throws IOException {
      this.oSavedFile = new RandomAccessFile(sName, "rws");
      this.oSavedFile.seek(nPos);
      this.sName = sName;
      this.nPos = nPos;
   }

   public synchronized void write(byte[] b, int off, int len) throws IOException {
      this.oSavedFile.write(b, off, len);
   }

   public void close() throws IOException {
      this.oSavedFile.close();
   }
}
