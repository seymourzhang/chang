package com.chang.uclass.bytecode;

import com.chang.uclass.bytecode.ClassStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class FeatureCodec {
   public static final FeatureCodec DEFAULT_COMMANDLINE_CODEC = new FeatureCodec(';', '=');
   private final char kvSegmentSeparator;
   private final char kvSeparator;
   private static final char ESCAPE_PREFIX_CHAR = '\\';

   public FeatureCodec(char kvSegmentSeparator, char kvSeparator) {
      if (CheckUtils.isIn('\\', kvSegmentSeparator, kvSeparator)) {
         throw new IllegalArgumentException("separator can not init to '\\'.");
      } else {
         this.kvSegmentSeparator = kvSegmentSeparator;
         this.kvSeparator = kvSeparator;
      }
   }

   public String toString(Map<String, String> map) {
      StringBuilder featureSB = (new StringBuilder()).append(this.kvSegmentSeparator);
      if (null != map && !map.isEmpty()) {
         Iterator var3 = map.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var3.next();
            featureSB.append(this.escapeEncode((String)entry.getKey())).append(this.kvSeparator).append(this.escapeEncode((String)entry.getValue())).append(this.kvSegmentSeparator);
         }

         return featureSB.toString();
      } else {
         return featureSB.toString();
      }
   }

   public Map<String, String> toMap(String featureString) {
      Map<String, String> map = new HashMap();
      if (ClassStringUtils.isBlank(featureString)) {
         return map;
      } else {
         String[] var3 = this.escapeSplit(featureString, this.kvSegmentSeparator);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String kv = var3[var5];
            if (!ClassStringUtils.isBlank(kv)) {
               String[] ar = this.escapeSplit(kv, this.kvSeparator);
               if (ar.length == 2) {
                  String k = ar[0];
                  String v = ar[1];
                  if (!ClassStringUtils.isBlank(k) && !ClassStringUtils.isBlank(v)) {
                     map.put(this.escapeDecode(k), this.escapeDecode(v));
                  }
               }
            }
         }

         return map;
      }
   }

   private String escapeEncode(String string) {
      StringBuilder returnSB = new StringBuilder();
      char[] var3 = string.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char c = var3[var5];
         if (CheckUtils.isIn(c, this.kvSegmentSeparator, this.kvSeparator, '\\')) {
            returnSB.append('\\');
         }

         returnSB.append(c);
      }

      return returnSB.toString();
   }

   private String escapeDecode(String string) {
      StringBuilder segmentSB = new StringBuilder();
      int stringLength = string.length();

      for(int index = 0; index < stringLength; ++index) {
         char c = string.charAt(index);
         if (CheckUtils.isEquals(c, '\\') && index < stringLength - 1) {
            ++index;
            char nextChar = string.charAt(index);
            if (CheckUtils.isIn(nextChar, this.kvSegmentSeparator, this.kvSeparator, '\\')) {
               segmentSB.append(nextChar);
            } else {
               segmentSB.append(c);
               segmentSB.append(nextChar);
            }
         } else {
            segmentSB.append(c);
         }
      }

      return segmentSB.toString();
   }

   private String[] escapeSplit(String string, char splitEscapeChar) {
      ArrayList<String> segmentArrayList = new ArrayList();
      Stack<Character> decodeStack = new Stack();
      int stringLength = string.length();

      for(int index = 0; index < stringLength; ++index) {
         boolean isArchive = false;
         char c = string.charAt(index);
         if (CheckUtils.isEquals(c, '\\')) {
            decodeStack.push(c);
            if (index < stringLength - 1) {
               ++index;
               char nextChar = string.charAt(index);
               decodeStack.push(nextChar);
            }
         } else if (CheckUtils.isEquals(c, splitEscapeChar)) {
            isArchive = true;
         } else {
            decodeStack.push(c);
         }

         if (isArchive || index == stringLength - 1) {
            StringBuilder segmentSB = new StringBuilder(decodeStack.size());

            while(!decodeStack.isEmpty()) {
               segmentSB.append(decodeStack.pop());
            }

            segmentArrayList.add(segmentSB.reverse().toString().trim());
         }
      }

      return (String[])segmentArrayList.toArray(new String[0]);
   }
}
