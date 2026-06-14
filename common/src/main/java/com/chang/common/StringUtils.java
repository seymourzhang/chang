package com.chang.common;

import com.chang.common.convert.StringConvertManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {
   public static final String EMPTY_STRING = "";
   public static final int INDEX_NOT_FOUND = -1;
   public static final String[] EMPTY_STRING_ARRAY = new String[0];
   private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
   private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)");
   private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
   private static final Pattern PARAMETERS_PATTERN = Pattern.compile("^\\[((\\s*\\{\\s*[\\w_\\-\\.]+\\s*:\\s*.+?\\s*\\}\\s*,?\\s*)+)\\s*\\]$");
   private static final Pattern PAIR_PARAMETERS_PATTERN = Pattern.compile("^\\{\\s*([\\w-_\\.]+)\\s*:\\s*(.+)\\s*\\}$");
   private static final int PAD_LIMIT = 8192;
   private static final byte[] HEX2B = new byte[128];
   public static final char EQUAL_CHAR = '=';
   public static final String EQUAL = String.valueOf('=');
   public static final char AND_CHAR = '&';
   public static final String AND = String.valueOf('&');
   public static final char SEMICOLON_CHAR = ';';
   public static final String SEMICOLON = String.valueOf(';');
   public static final char QUESTION_MASK_CHAR = '?';
   public static final String QUESTION_MASK = String.valueOf('?');
   public static final char SLASH_CHAR = '/';
   public static final String SLASH = String.valueOf('/');
   public static final char HYPHEN_CHAR = '-';
   public static final String HYPHEN = String.valueOf('-');

   private StringUtils() {
   }

   public static int length(CharSequence cs) {
      return cs == null ? 0 : cs.length();
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

   public static String repeat(String str, String separator, int repeat) {
      if (str != null && separator != null) {
         String result = repeat(str + separator, repeat);
         return removeEnd(result, separator);
      } else {
         return repeat(str, repeat);
      }
   }

   public static String removeEnd(String str, String remove) {
      if (isAnyEmpty(str, remove)) {
         return str;
      } else {
         return str.endsWith(remove) ? str.substring(0, str.length() - remove.length()) : str;
      }
   }

   public static String repeat(char ch, int repeat) {
      char[] buf = new char[repeat];

      for(int i = repeat - 1; i >= 0; --i) {
         buf[i] = ch;
      }

      return new String(buf);
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

   public static String replace(String text, String searchString, String replacement) {
      return replace(text, searchString, replacement, -1);
   }

   public static String replace(String text, String searchString, String replacement, int max) {
      if (!isAnyEmpty(text, searchString) && replacement != null && max != 0) {
         int start = 0;
         int end = text.indexOf(searchString, start);
         if (end == -1) {
            return text;
         } else {
            int replLength = searchString.length();
            int increase = replacement.length() - replLength;
            increase = increase < 0 ? 0 : increase;
            increase *= max < 0 ? 16 : (max > 64 ? 64 : max);

            StringBuilder buf;
            for(buf = new StringBuilder(text.length() + increase); end != -1; end = text.indexOf(searchString, start)) {
               buf.append(text, start, end).append(replacement);
               start = end + replLength;
               --max;
               if (max == 0) {
                  break;
               }
            }

            buf.append(text.substring(start));
            return buf.toString();
         }
      } else {
         return text;
      }
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

   public static boolean isEmpty(String str) {
      return str == null || str.isEmpty();
   }

   public static boolean isNoneEmpty(String... ss) {
      if (ArrayUtils.isEmpty(ss)) {
         return false;
      } else {
         String[] var1 = ss;
         int var2 = ss.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String s = var1[var3];
            if (isEmpty(s)) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAnyEmpty(String... ss) {
      return !isNoneEmpty(ss);
   }

   public static boolean isNotEmpty(String str) {
      return !isEmpty(str);
   }

   public static boolean isEquals(String s1, String s2) {
      if (s1 == null && s2 == null) {
         return true;
      } else {
         return s1 != null && s2 != null ? s1.equals(s2) : false;
      }
   }

   public static boolean isInteger(String str) {
      return isNotEmpty(str) && INT_PATTERN.matcher(str).matches();
   }

   public static int parseInteger(String str) {
      return isInteger(str) ? Integer.parseInt(str) : 0;
   }

   public static boolean isJavaIdentifier(String s) {
      if (!isEmpty(s) && Character.isJavaIdentifierStart(s.charAt(0))) {
         for(int i = 1; i < s.length(); ++i) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isContains(String values, String value) {
      return isNotEmpty(values) && isContains(CommonConstants.COMMA_SPLIT_PATTERN.split(values), value);
   }

   public static boolean isContains(String str, char ch) {
      return isNotEmpty(str) && str.indexOf(ch) >= 0;
   }

   public static boolean isNotContains(String str, char ch) {
      return !isContains(str, ch);
   }

   public static boolean isContains(String[] values, String value) {
      if (isNotEmpty(value) && ArrayUtils.isNotEmpty(values)) {
         String[] var2 = values;
         int var3 = values.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String v = var2[var4];
            if (value.equals(v)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isNumeric(String str, boolean allowDot) {
      if (str != null && !str.isEmpty()) {
         boolean hasDot = false;
         int sz = str.length();

         for(int i = 0; i < sz; ++i) {
            if (str.charAt(i) == '.') {
               if (hasDot || !allowDot) {
                  return false;
               }

               hasDot = true;
            } else if (!Character.isDigit(str.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static String toString(Throwable e) {
      UnsafeStringWriter w = new UnsafeStringWriter();
      PrintWriter p = new PrintWriter(w);
      p.print(e.getClass().getName());
      if (e.getMessage() != null) {
         p.print(": " + e.getMessage());
      }

      p.println();

      String var3;
      try {
         e.printStackTrace(p);
         var3 = w.toString();
      } finally {
         p.close();
      }

      return var3;
   }

   public static String toString(String msg, Throwable e) {
      UnsafeStringWriter w = new UnsafeStringWriter();
      w.write(msg + "\n");
      PrintWriter p = new PrintWriter(w);

      String var4;
      try {
         e.printStackTrace(p);
         var4 = w.toString();
      } finally {
         p.close();
      }

      return var4;
   }

   public static String translate(String src, String from, String to) {
      if (isEmpty(src)) {
         return src;
      } else {
         StringBuilder sb = null;
         int i = 0;

         for(int len = src.length(); i < len; ++i) {
            char c = src.charAt(i);
            int ix = from.indexOf(c);
            if (ix == -1) {
               if (sb != null) {
                  sb.append(c);
               }
            } else {
               if (sb == null) {
                  sb = new StringBuilder(len);
                  sb.append(src, 0, i);
               }

               if (ix < to.length()) {
                  sb.append(to.charAt(ix));
               }
            }
         }

         return sb == null ? src : sb.toString();
      }
   }

   public static String[] split(String str, char ch) {
      return isEmpty(str) ? EMPTY_STRING_ARRAY : (String[])splitToList0(str, ch).toArray(EMPTY_STRING_ARRAY);
   }

   private static List<String> splitToList0(String str, char ch) {
      List<String> result = new ArrayList();
      int ix = 0;
      int len = str.length();

      for(int i = 0; i < len; ++i) {
         if (str.charAt(i) == ch) {
            result.add(str.substring(ix, i));
            ix = i + 1;
         }
      }

      if (ix >= 0) {
         result.add(str.substring(ix));
      }

      return result;
   }

   public static List<String> splitToList(String str, char ch) {
      return isEmpty(str) ? Collections.emptyList() : splitToList0(str, ch);
   }

   public static Set<String> splitToSet(String value, char separatorChar) {
      return splitToSet(value, separatorChar, false);
   }

   public static Set<String> splitToSet(String value, char separatorChar, boolean trimElements) {
      List<String> values = splitToList(value, separatorChar);
      int size = values.size();
      if (size < 1) {
         return Collections.emptySet();
      } else {
         return (Set)(!trimElements ? new LinkedHashSet(values) : Collections.unmodifiableSet((Set)values.stream().map(String::trim).collect(LinkedHashSet::new, Set::add, Set::addAll)));
      }
   }

   public static String join(String[] array) {
      if (ArrayUtils.isEmpty(array)) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder();
         String[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            sb.append(s);
         }

         return sb.toString();
      }
   }

   public static String join(String[] array, char split) {
      if (ArrayUtils.isEmpty(array)) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < array.length; ++i) {
            if (i > 0) {
               sb.append(split);
            }

            sb.append(array[i]);
         }

         return sb.toString();
      }
   }

   public static String join(String[] array, String split) {
      if (ArrayUtils.isEmpty(array)) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < array.length; ++i) {
            if (i > 0) {
               sb.append(split);
            }

            sb.append(array[i]);
         }

         return sb.toString();
      }
   }

   public static String join(Collection<String> coll, String split) {
      if (org.apache.commons.collections.CollectionUtils.isEmpty(coll)) {
         return "";
      } else {
         StringBuilder sb = new StringBuilder();
         boolean isFirst = true;

         String s;
         for(Iterator var4 = coll.iterator(); var4.hasNext(); sb.append(s)) {
            s = (String)var4.next();
            if (isFirst) {
               isFirst = false;
            } else {
               sb.append(split);
            }
         }

         return sb.toString();
      }
   }

   private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
      String[] tmp = str.split(itemSeparator);
      Map<String, String> map = new HashMap(tmp.length);

      for(int i = 0; i < tmp.length; ++i) {
         Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
         if (matcher.matches()) {
            map.put(matcher.group(1), matcher.group(2));
         }
      }

      return map;
   }

   public static String getQueryStringValue(String qs, String key) {
      Map<String, String> map = parseQueryString(qs);
      return (String)map.get(key);
   }

   public static Map<String, String> parseQueryString(String qs) {
      return (Map)(isEmpty(qs) ? new HashMap() : parseKeyValuePair(qs, "\\&"));
   }

   public static String getServiceKey(Map<String, String> ps) {
      StringBuilder buf = new StringBuilder();
      String group = (String)ps.get("group");
      if (isNotEmpty(group)) {
         buf.append(group).append("/");
      }

      buf.append((String)ps.get("interface"));
      String version = (String)ps.get("version");
      if (isNotEmpty(group)) {
         buf.append(":").append(version);
      }

      return buf.toString();
   }

   public static String toQueryString(Map<String, String> ps) {
      StringBuilder buf = new StringBuilder();
      if (ps != null && ps.size() > 0) {
         Iterator var2 = (new TreeMap(ps)).entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var2.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            if (isNoneEmpty(key, value)) {
               if (buf.length() > 0) {
                  buf.append("&");
               }

               buf.append(key);
               buf.append("=");
               buf.append(value);
            }
         }
      }

      return buf.toString();
   }

   public static String camelToSplitName(String camelName, String split) {
      if (isEmpty(camelName)) {
         return camelName;
      } else {
         StringBuilder buf = null;

         for(int i = 0; i < camelName.length(); ++i) {
            char ch = camelName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
               if (buf == null) {
                  buf = new StringBuilder();
                  if (i > 0) {
                     buf.append(camelName, 0, i);
                  }
               }

               if (i > 0) {
                  buf.append(split);
               }

               buf.append(Character.toLowerCase(ch));
            } else if (buf != null) {
               buf.append(ch);
            }
         }

         return buf == null ? camelName : buf.toString();
      }
   }

   public static String trim(String str) {
      return str == null ? null : str.trim();
   }

   public static String toURLKey(String key) {
      return key.toLowerCase().replaceAll("_|-", ".");
   }

   public static String toOSStyleKey(String key) {
      key = key.toUpperCase().replaceAll("\\.", "_");
      if (!key.startsWith("DUBBO_")) {
         key = "DUBBO_" + key;
      }

      return key;
   }

   public static boolean isAllUpperCase(String str) {
      if (str != null && !isEmpty(str)) {
         int sz = str.length();

         for(int i = 0; i < sz; ++i) {
            if (!Character.isUpperCase(str.charAt(i))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
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
         List<String> result = new ArrayList();
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

   public static String arrayToDelimitedString(Object[] arr, String delim) {
      if (ArrayUtils.isEmpty(arr)) {
         return "";
      } else if (arr.length == 1) {
         return nullSafeToString(arr[0]);
      } else {
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < arr.length; ++i) {
            if (i > 0) {
               sb.append(delim);
            }

            sb.append(arr[i]);
         }

         return sb.toString();
      }
   }

   public static String deleteAny(String inString, String charsToDelete) {
      if (isNotEmpty(inString) && isNotEmpty(charsToDelete)) {
         StringBuilder sb = new StringBuilder(inString.length());

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

   public static String[] toStringArray(Collection<String> collection) {
      return (String[])((String[])collection.toArray(new String[0]));
   }

   public static String nullSafeToString(Object obj) {
      if (obj == null) {
         return "null";
      } else if (obj instanceof String) {
         return (String)obj;
      } else {
         String str = obj.toString();
         return str != null ? str : "";
      }
   }

   public static Map<String, String> parseParameters(String rawParameters) {
      Matcher matcher = PARAMETERS_PATTERN.matcher(rawParameters);
      if (!matcher.matches()) {
         return Collections.emptyMap();
      } else {
         String pairs = matcher.group(1);
         String[] pairArr = pairs.split("\\s*,\\s*");
         Map<String, String> parameters = new HashMap();
         String[] var5 = pairArr;
         int var6 = pairArr.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String pair = var5[var7];
            Matcher pairMatcher = PAIR_PARAMETERS_PATTERN.matcher(pair);
            if (pairMatcher.matches()) {
               parameters.put(pairMatcher.group(1), pairMatcher.group(2));
            }
         }

         return parameters;
      }
   }

   public static int decodeHexNibble(char c) {
      byte[] hex2b = HEX2B;
      return c < hex2b.length ? hex2b[c] : -1;
   }

   public static byte decodeHexByte(CharSequence s, int pos) {
      int hi = decodeHexNibble(s.charAt(pos));
      int lo = decodeHexNibble(s.charAt(pos + 1));
      if (hi != -1 && lo != -1) {
         return (byte)((hi << 4) + lo);
      } else {
         throw new IllegalArgumentException(String.format("invalid hex byte '%s' at index %d of '%s'", s.subSequence(pos, pos + 2), pos, s));
      }
   }

   public static String toCommaDelimitedString(String one, String... others) {
      String another = arrayToDelimitedString(others, ",");
      return isEmpty(another) ? one : one + "," + another;
   }

   public static Object convert(String source, Class<?> targetType) {
      return StringConvertManager.getConverter(source, targetType);
   }

   public static Object convert(String source, Class<?> targetType, Class<?> elementType) {
      return StringConvertManager.getMultiConverter(source, targetType, elementType);
   }

   static {
      Arrays.fill(HEX2B, (byte)-1);
      HEX2B[48] = 0;
      HEX2B[49] = 1;
      HEX2B[50] = 2;
      HEX2B[51] = 3;
      HEX2B[52] = 4;
      HEX2B[53] = 5;
      HEX2B[54] = 6;
      HEX2B[55] = 7;
      HEX2B[56] = 8;
      HEX2B[57] = 9;
      HEX2B[65] = 10;
      HEX2B[66] = 11;
      HEX2B[67] = 12;
      HEX2B[68] = 13;
      HEX2B[69] = 14;
      HEX2B[70] = 15;
      HEX2B[97] = 10;
      HEX2B[98] = 11;
      HEX2B[99] = 12;
      HEX2B[100] = 13;
      HEX2B[101] = 14;
      HEX2B[102] = 15;
   }
}
