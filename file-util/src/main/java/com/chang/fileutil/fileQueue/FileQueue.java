package com.chang.fileutil.fileQueue;

import cn.hutool.core.io.FileUtil;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileQueue {
   private static final Logger log = LoggerFactory.getLogger(FileQueue.class);
   private final ConcurrentLinkedQueue<FileIndex> fileList = new ConcurrentLinkedQueue();
   private String filePath;
   private AtomicInteger currentIndex = new AtomicInteger(0);
   private final RandomAccessFile randomAccessTargetFile;
   private final FileChannel targetFileChannel;

   public FileQueue(String filePath) throws Exception {
      this.filePath = filePath;
      this.randomAccessTargetFile = new RandomAccessFile(filePath, "rw");
      this.targetFileChannel = this.randomAccessTargetFile.getChannel();
   }

   public void offer(byte[] data) {
      if (this.fileList.isEmpty()) {
         this.currentIndex.set(0);
      }

      this.fileWrite(data, this.currentIndex.get());
      FileIndex fileIndex = new FileIndex();
      fileIndex.setStartIndex(this.currentIndex.get());
      fileIndex.setLens(data.length);
      this.fileList.offer(fileIndex);
      this.currentIndex.addAndGet(data.length);
   }

   public byte[] poll() {
      if (this.fileList.isEmpty()) {
         this.currentIndex.set(0);
         throw new RuntimeException("poll is Empty!");
      } else {
         FileIndex fileIndex = (FileIndex)this.fileList.poll();
         return this.fileRead(fileIndex.getStartIndex(), fileIndex.getLens());
      }
   }

   public Boolean isEmpty() {
      return this.fileList.isEmpty();
   }

   public int size() {
      return this.fileList.size();
   }

   public void fileWrite(byte[] src, int index) {
      try {
         MappedByteBuffer map = this.targetFileChannel.map(MapMode.READ_WRITE, (long)index, (long)src.length);
         map.put(src);
         map.force();
      } catch (Exception var4) {
         log.error("fileWrite: ", var4);
      }

   }

   public byte[] fileRead(int index, int length) {
      try {
         MappedByteBuffer map = this.targetFileChannel.map(MapMode.READ_WRITE, (long)index, (long)length);
         byte[] byteArr = new byte[length];
         map.get(byteArr);
         return byteArr;
      } catch (Exception var5) {
         log.error("fileRead: ", var5);
         return null;
      }
   }

    public void clear() throws Exception {
        MappedByteBuffer map = this.targetFileChannel.map(MapMode.READ_WRITE, 0L, this.targetFileChannel.size());
        this.randomAccessTargetFile.close();
        this.targetFileChannel.close();

        // 使用 Cleaner 清理 MappedByteBuffer，避免使用 sun.nio.ch 内部类
        cleanBuffer(map);

        FileUtil.del(this.filePath);
    }

    private static void cleanBuffer(MappedByteBuffer buffer) {
        if (buffer == null) {
            return;
        }

        try {
            Object cleaner = java.nio.ByteBuffer.allocateDirect(1).getClass()
                    .getMethod("cleaner")
                    .invoke(buffer);

            if (cleaner != null) {
                cleaner.getClass().getMethod("clean").invoke(cleaner);
            }
        } catch (Exception e1) {
            try {
                // Java 8 及更早版本使用 reflection 访问 sun.misc.Cleaner
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                java.lang.reflect.Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
                theUnsafeField.setAccessible(true);
                Object unsafe = theUnsafeField.get(null);

                java.lang.reflect.Method invokeCleanerMethod = unsafe.getClass()
                        .getMethod("invokeCleaner", java.nio.ByteBuffer.class);
                invokeCleanerMethod.invoke(unsafe, buffer);
            } catch (Exception e2) {
                // 如果都失败，依赖 GC 最终清理
                log.warn("无法显式清理 MappedByteBuffer，将依赖 GC 清理", e2);
            }
        }
    }
}
