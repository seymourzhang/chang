package com.chang.uclass.bytecode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public abstract class ClassStringUtils {
   private static final Logger log = LoggerFactory.getLogger(ClassStringUtils.class);
   private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   private static final int PAD_LIMIT = 8192;
   public static final int INDEX_NOT_FOUND = -1;

   public static String cause(Throwable t) {
      return null != t.getCause() ? cause(t.getCause()) : t.getMessage();
   }

   public static String objectToString(Object obj) {
      if (null == obj) {
         return "";
      } else {
         try {
            return obj.toString();
         } catch (Throwable var2) {
            log.error("objectToString error, obj class: {}", obj.getClass(), var2);
            return "ERROR DATA!!! Method toString() throw exception. obj class: " + obj.getClass() + ", exception class: " + var2.getClass() + ", exception message: " + var2.getMessage();
         }
      }
   }

   public static String classname(Class<?> clazz) {
      if (clazz.isArray()) {
         StringBuilder sb = new StringBuilder(clazz.getName());
         sb.delete(0, 2);
         if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ';') {
            sb.deleteCharAt(sb.length() - 1);
         }

         sb.append("[]");
         return sb.toString();
      } else {
         return clazz.getName();
      }
   }

   public static String normalizeClassName(String className) {
      return StringUtils.replace(className, "/", ".");
   }

   public static String concat(String separator, Class<?>... types) {
      if (types != null && types.length != 0) {
         StringBuilder builder = new StringBuilder();

         for(int i = 0; i < types.length; ++i) {
            builder.append(classname(types[i]));
            if (i < types.length - 1) {
               builder.append(separator);
            }
         }

         return builder.toString();
      } else {
         return "";
      }
   }

   public static String concat(String separator, String... strs) {
      if (strs != null && strs.length != 0) {
         StringBuilder builder = new StringBuilder();

         for(int i = 0; i < strs.length; ++i) {
            builder.append(strs[i]);
            if (i < strs.length - 1) {
               builder.append(separator);
            }
         }

         return builder.toString();
      } else {
         return "";
      }
   }

   public static String modifier(int mod, char splitter) {
      StringBuilder sb = new StringBuilder();
      if (Modifier.isAbstract(mod)) {
         sb.append("abstract").append(splitter);
      }

      if (Modifier.isFinal(mod)) {
         sb.append("final").append(splitter);
      }

      if (Modifier.isInterface(mod)) {
         sb.append("interface").append(splitter);
      }

      if (Modifier.isNative(mod)) {
         sb.append("native").append(splitter);
      }

      if (Modifier.isPrivate(mod)) {
         sb.append("private").append(splitter);
      }

      if (Modifier.isProtected(mod)) {
         sb.append("protected").append(splitter);
      }

      if (Modifier.isPublic(mod)) {
         sb.append("public").append(splitter);
      }

      if (Modifier.isStatic(mod)) {
         sb.append("static").append(splitter);
      }

      if (Modifier.isStrict(mod)) {
         sb.append("strict").append(splitter);
      }

      if (Modifier.isSynchronized(mod)) {
         sb.append("synchronized").append(splitter);
      }

      if (Modifier.isTransient(mod)) {
         sb.append("transient").append(splitter);
      }

      if (Modifier.isVolatile(mod)) {
         sb.append("volatile").append(splitter);
      }

      if (sb.length() > 0) {
         sb.deleteCharAt(sb.length() - 1);
      }

      return sb.toString();
   }

   public static String wrap(String string, int width) {
      StringBuilder sb = new StringBuilder();
      char[] buffer = string.toCharArray();
      int count = 0;
      char[] var5 = buffer;
      int var6 = buffer.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         char c = var5[var7];
         if (count == width) {
            count = 0;
            sb.append('\n');
            if (c == '\n') {
               continue;
            }
         }

         if (c == '\n') {
            count = 0;
         } else {
            ++count;
         }

         sb.append(c);
      }

      return sb.toString();
   }

   public static boolean isEmpty(Object str) {
      return str == null || "".equals(str);
   }

   public static boolean hasLength(CharSequence str) {
      return str != null && str.length() > 0;
   }

   public static boolean hasLength(String str) {
      return hasLength((CharSequence)str);
   }

   public static boolean hasText(CharSequence str) {
      if (!hasLength(str)) {
         return false;
      } else {
         int strLen = str.length();

         for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean hasText(String str) {
      return hasText((CharSequence)str);
   }

   public static boolean containsWhitespace(CharSequence str) {
      if (!hasLength(str)) {
         return false;
      } else {
         int strLen = str.length();

         for(int i = 0; i < strLen; ++i) {
            if (Character.isWhitespace(str.charAt(i))) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean containsWhitespace(String str) {
      return containsWhitespace((CharSequence)str);
   }

   public static String trimWhitespace(String str) {
      if (!hasLength(str)) {
         return str;
      } else {
         StringBuilder sb = new StringBuilder(str);

         while(sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
         }

         while(sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
         }

         return sb.toString();
      }
   }

   public static String trimAllWhitespace(String str) {
      if (!hasLength(str)) {
         return str;
      } else {
         StringBuilder sb = new StringBuilder(str);
         int index = 0;

         while(sb.length() > index) {
            if (Character.isWhitespace(sb.charAt(index))) {
               sb.deleteCharAt(index);
            } else {
               ++index;
            }
         }

         return sb.toString();
      }
   }

   public static String trimLeadingWhitespace(String str) {
      if (!hasLength(str)) {
         return str;
      } else {
         StringBuilder sb = new StringBuilder(str);

         while(sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
         }

         return sb.toString();
      }
   }

   public static String trimTrailingWhitespace(String str) {
      if (!hasLength(str)) {
         return str;
      } else {
         StringBuilder sb = new StringBuilder(str);

         while(sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
         }

         return sb.toString();
      }
   }

   public static String trimLeadingCharacter(String str, char leadingCharacter) {
      if (!hasLength(str)) {
         return str;
      } else {
         StringBuilder sb = new StringBuilder(str);

         while(sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
            sb.deleteCharAt(0);
         }

         return sb.toString();
      }
   }

   public static String trimTrailingCharacter(String str, char trailingCharacter) {
      if (!hasLength(str)) {
         return str;
      } else {
         StringBuilder sb = new StringBuilder(str);

         while(sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
            sb.deleteCharAt(sb.length() - 1);
         }

         return sb.toString();
      }
   }

   public static boolean startsWithIgnoreCase(String str, String prefix) {
      if (str != null && prefix != null) {
         if (str.startsWith(prefix)) {
            return true;
         } else if (str.length() < prefix.length()) {
            return false;
         } else {
            String lcStr = str.substring(0, prefix.length()).toLowerCase();
            String lcPrefix = prefix.toLowerCase();
            return lcStr.equals(lcPrefix);
         }
      } else {
         return false;
      }
   }

   public static boolean endsWithIgnoreCase(String str, String suffix) {
      if (str != null && suffix != null) {
         if (str.endsWith(suffix)) {
            return true;
         } else if (str.length() < suffix.length()) {
            return false;
         } else {
            String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
            String lcSuffix = suffix.toLowerCase();
            return lcStr.equals(lcSuffix);
         }
      } else {
         return false;
      }
   }

   public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
      for(int j = 0; j < substring.length(); ++j) {
         int i = index + j;
         if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
            return false;
         }
      }

      return true;
   }

   public static String substringAfter(String str, String separator) {
      if (isEmpty(str)) {
         return str;
      } else if (separator == null) {
         return "";
      } else {
         int pos = str.indexOf(separator);
         return pos == -1 ? "" : str.substring(pos + separator.length());
      }
   }

   public static String substringBeforeLast(String str, String separator) {
      if (!isEmpty(str) && !isEmpty(separator)) {
         int pos = str.lastIndexOf(separator);
         return pos == -1 ? str : str.substring(0, pos);
      } else {
         return str;
      }
   }

   public static String substringBefore(String str, String separator) {
      if (!isEmpty(str) && separator != null) {
         if (separator.isEmpty()) {
            return "";
         } else {
            int pos = str.indexOf(separator);
            return pos == -1 ? str : str.substring(0, pos);
         }
      } else {
         return str;
      }
   }

   public static String substringAfterLast(String str, String separator) {
      if (isEmpty(str)) {
         return str;
      } else if (isEmpty(separator)) {
         return "";
      } else {
         int pos = str.lastIndexOf(separator);
         return pos != -1 && pos != str.length() - separator.length() ? str.substring(pos + separator.length()) : "";
      }
   }

   public static int countOccurrencesOf(String str, String sub) {
      if (str != null && sub != null && str.length() != 0 && sub.length() != 0) {
         int count = 0;

         int idx;
         for(int pos = 0; (idx = str.indexOf(sub, pos)) != -1; pos = idx + sub.length()) {
            ++count;
         }

         return count;
      } else {
         return 0;
      }
   }

   public static String replace(String inString, String oldPattern, String newPattern) {
      if (hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
         int pos = 0;
         int index = inString.indexOf(oldPattern);
         if (index < 0) {
            return inString;
         } else {
            StringBuilder sb = new StringBuilder();

            for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
               sb.append(inString, pos, index);
               sb.append(newPattern);
               pos = index + patLen;
            }

            sb.append(inString.substring(pos));
            return sb.toString();
         }
      } else {
         return inString;
      }
   }

   public static String delete(String inString, String pattern) {
      return replace(inString, pattern, "");
   }

   public static String deleteAny(String inString, String charsToDelete) {
      if (hasLength(inString) && hasLength(charsToDelete)) {
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < inString.length(); ++i) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
               sb.append(c);
            }
         }

         return sb.toString();
      } else {
         return inString;
      }
   }

   public static String quote(String str) {
      return str != null ? "'" + str + "'" : null;
   }

   public static Object quoteIfString(Object obj) {
      return obj instanceof String ? quote((String)obj) : obj;
   }

   public static String unqualify(String qualifiedName) {
      return unqualify(qualifiedName, '.');
   }

   public static String unqualify(String qualifiedName, char separator) {
      return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
   }

   public static String capitalize(String str) {
      return changeFirstCharacterCase(str, true);
   }

   public static String uncapitalize(String str) {
      return changeFirstCharacterCase(str, false);
   }

   private static String changeFirstCharacterCase(String str, boolean capitalize) {
      if (str != null && str.length() != 0) {
         StringBuilder sb = new StringBuilder(str.length());
         if (capitalize) {
            sb.append(Character.toUpperCase(str.charAt(0)));
         } else {
            sb.append(Character.toLowerCase(str.charAt(0)));
         }

         sb.append(str.substring(1));
         return sb.toString();
      } else {
         return str;
      }
   }

   public static String[] toStringArray(Collection<String> collection) {
      return collection == null ? null : (String[])((String[])collection.toArray(new String[0]));
   }

   public static String[] split(String toSplit, String delimiter) {
      if (hasLength(toSplit) && hasLength(delimiter)) {
         int offset = toSplit.indexOf(delimiter);
         if (offset < 0) {
            return null;
         } else {
            String beforeDelimiter = toSplit.substring(0, offset);
            String afterDelimiter = toSplit.substring(offset + delimiter.length());
            return new String[]{beforeDelimiter, afterDelimiter};
         }
      } else {
         return null;
      }
   }

   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
      return splitArrayElementsIntoProperties(array, delimiter, (String)null);
   }

   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {
      if (ObjectUtils.isEmpty(array)) {
         return null;
      } else {
         Properties result = new Properties();
         String[] var4 = array;
         int var5 = array.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String element = var4[var6];
            if (charsToDelete != null) {
               element = deleteAny(element, charsToDelete);
            }

            String[] splittedElement = split(element, delimiter);
            if (splittedElement != null) {
               result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
            }
         }

         return result;
      }
   }

   public static String[] tokenizeToStringArray(String str, String delimiters) {
      return tokenizeToStringArray(str, delimiters, true, true);
   }

   public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
      if (str == null) {
         return null;
      } else {
         StringTokenizer st = new StringTokenizer(str, delimiters);
         ArrayList<String> tokens = new ArrayList();

         while(true) {
            String token;
            do {
               if (!st.hasMoreTokens()) {
                  return toStringArray(tokens);
               }

               token = st.nextToken();
               if (trimTokens) {
                  token = token.trim();
               }
            } while(ignoreEmptyTokens && token.length() <= 0);

            tokens.add(token);
         }
      }
   }

   public static String[] delimitedListToStringArray(String str, String delimiter) {
      return delimitedListToStringArray(str, delimiter, (String)null);
   }

   public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
      if (str == null) {
         return new String[0];
      } else if (delimiter == null) {
         return new String[]{str};
      } else {
         ArrayList<String> result = new ArrayList();
         int pos;
         if ("".equals(delimiter)) {
            for(pos = 0; pos < str.length(); ++pos) {
               result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
            }
         } else {
            int delPos;
            for(pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
               result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
            }

            if (str.length() > 0 && pos <= str.length()) {
               result.add(deleteAny(str.substring(pos), charsToDelete));
            }
         }

         return toStringArray(result);
      }
   }

   public static String[] commaDelimitedListToStringArray(String str) {
      return delimitedListToStringArray(str, ",");
   }

   public static Set<String> commaDelimitedListToSet(String str) {
      String[] tokens = commaDelimitedListToStringArray(str);
      return new TreeSet(Arrays.asList(tokens));
   }

   public static String join(Object[] array, String separator) {
      if (separator == null) {
         separator = "";
      }

      int arraySize = array.length;
      int bufSize = arraySize == 0 ? 0 : (array[0].toString().length() + separator.length()) * arraySize;
      StringBuilder buf = new StringBuilder(bufSize);

      for(int i = 0; i < arraySize; ++i) {
         if (i > 0) {
            buf.append(separator);
         }

         buf.append(array[i]);
      }

      return buf.toString();
   }

   public static boolean isBlank(CharSequence cs) {
      int strLen;
      if (cs != null && (strLen = cs.length()) != 0) {
         for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(cs.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public static String repeat(char ch, int repeat) {
      char[] buf = new char[repeat];

      for(int i = repeat - 1; i >= 0; --i) {
         buf[i] = ch;
      }

      return new String(buf);
   }

   public static String repeat(String str, int repeat) {
      if (str == null) {
         return null;
      } else if (repeat <= 0) {
         return "";
      } else {
         int inputLength = str.length();
         if (repeat != 1 && inputLength != 0) {
            if (inputLength == 1 && repeat <= 8192) {
               return repeat(str.charAt(0), repeat);
            } else {
               int outputLength = inputLength * repeat;
               switch (inputLength) {
                  case 1:
                     return repeat(str.charAt(0), repeat);
                  case 2:
                     char ch0 = str.charAt(0);
                     char ch1 = str.charAt(1);
                     char[] output2 = new char[outputLength];

                     for(int i = repeat * 2 - 2; i >= 0; --i) {
                        output2[i] = ch0;
                        output2[i + 1] = ch1;
                        --i;
                     }

                     return new String(output2);
                  default:
                     StringBuilder buf = new StringBuilder(outputLength);

                     for(int i = 0; i < repeat; ++i) {
                        buf.append(str);
                     }

                     return buf.toString();
               }
            }
         } else {
            return str;
         }
      }
   }

   public static int length(CharSequence cs) {
      return cs == null ? 0 : cs.length();
   }

   public static String stripEnd(String str, String stripChars) {
      int end;
      if (str != null && (end = str.length()) != 0) {
         if (stripChars == null) {
            while(end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
               --end;
            }
         } else {
            if (stripChars.isEmpty()) {
               return str;
            }

            while(end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
               --end;
            }
         }

         return str.substring(0, end);
      } else {
         return str;
      }
   }

   public static String classLoaderHash(Class<?> clazz) {
      return clazz != null && clazz.getClassLoader() != null ? Integer.toHexString(clazz.getClassLoader().hashCode()) : "null";
   }

   public static String humanReadableByteCount(long bytes) {
      return bytes < 1024L ? bytes + " B" : (bytes < 1048524L ? String.format("%.1f KiB", (double)bytes / 1024.0) : (bytes < 1073689395L ? String.format("%.1f MiB", (double)bytes / 1048576.0) : (bytes < 1099457940684L ? String.format("%.1f GiB", (double)bytes / 1.073741824E9) : (bytes < 1125844931261235L ? String.format("%.1f TiB", (double)bytes / 1.099511627776E12) : (bytes < 1152865209611504844L ? String.format("%.1f PiB", (double)(bytes >> 10) / 1.099511627776E12) : String.format("%.1f EiB", (double)(bytes >> 20) / 1.099511627776E12))))));
   }

   public static List<String> toLines(String text) {
      List<String> result = new ArrayList();
      BufferedReader reader = new BufferedReader(new StringReader(text));

      try {
         for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            result.add(line);
         }
      } catch (IOException var12) {
      } finally {
         try {
            reader.close();
         } catch (IOException var11) {
         }

      }

      return result;
   }

   public static String randomString(int length) {
      StringBuilder sb = new StringBuilder(length);

      for(int i = 0; i < length; ++i) {
         sb.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(ThreadLocalRandom.current().nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
      }

      return sb.toString();
   }

   public static String before(String text, String before) {
      int pos = text.indexOf(before);
      return pos == -1 ? null : text.substring(0, pos);
   }

   public static String after(String text, String after) {
      int pos = text.indexOf(after);
      return pos == -1 ? null : text.substring(pos + after.length());
   }

   public static String[] splitMethodInfo(String methodInfo) {
      int index = methodInfo.indexOf(124);
      return new String[]{methodInfo.substring(0, index), methodInfo.substring(index + 1)};
   }

   public static String[] splitInvokeInfo(String invokeInfo) {
      int index1 = invokeInfo.indexOf(124);
      int index2 = invokeInfo.indexOf(124, index1 + 1);
      int index3 = invokeInfo.indexOf(124, index2 + 1);
      return new String[]{invokeInfo.substring(0, index1), invokeInfo.substring(index1 + 1, index2), invokeInfo.substring(index2 + 1, index3), invokeInfo.substring(index3 + 1)};
   }
}
