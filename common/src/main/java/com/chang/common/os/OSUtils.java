package com.chang.common.os;

import java.util.Locale;

public class OSUtils {
   private static final String OPERATING_SYSTEM_NAME;
   private static final String OPERATING_SYSTEM_ARCH;
   private static final String UNKNOWN = "unknown";
   static PlatformEnum platform;
   static String arch;

   private OSUtils() {
   }

   public static boolean isWindows() {
      return platform == PlatformEnum.WINDOWS;
   }

   public static boolean isLinux() {
      return platform == PlatformEnum.LINUX;
   }

   public static boolean isMac() {
      return platform == PlatformEnum.MACOSX;
   }

   public static boolean isCygwinOrMinGW() {
      return isWindows() && (System.getenv("MSYSTEM") != null && System.getenv("MSYSTEM").startsWith("MINGW") || "/bin/bash".equals(System.getenv("SHELL")));
   }

   public static String arch() {
      return arch;
   }

   public static boolean isArm32() {
      return "arm_32".equals(arch);
   }

   public static boolean isArm64() {
      return "aarch_64".equals(arch);
   }

   private static String normalizeArch(String value) {
      value = normalize(value);
      if (value.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
         return "x86_64";
      } else if (value.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
         return "x86_32";
      } else if (value.matches("^(ia64w?|itanium64)$")) {
         return "itanium_64";
      } else if ("ia64n".equals(value)) {
         return "itanium_32";
      } else if (value.matches("^(sparc|sparc32)$")) {
         return "sparc_32";
      } else if (value.matches("^(sparcv9|sparc64)$")) {
         return "sparc_64";
      } else if (value.matches("^(arm|arm32)$")) {
         return "arm_32";
      } else if ("aarch64".equals(value)) {
         return "aarch_64";
      } else if (value.matches("^(mips|mips32)$")) {
         return "mips_32";
      } else if (value.matches("^(mipsel|mips32el)$")) {
         return "mipsel_32";
      } else if ("mips64".equals(value)) {
         return "mips_64";
      } else if ("mips64el".equals(value)) {
         return "mipsel_64";
      } else if (value.matches("^(ppc|ppc32)$")) {
         return "ppc_32";
      } else if (value.matches("^(ppcle|ppc32le)$")) {
         return "ppcle_32";
      } else if ("ppc64".equals(value)) {
         return "ppc_64";
      } else if ("ppc64le".equals(value)) {
         return "ppcle_64";
      } else if ("s390".equals(value)) {
         return "s390_32";
      } else {
         return "s390x".equals(value) ? "s390_64" : "unknown";
      }
   }

   private static String normalize(String value) {
      return value == null ? "" : value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
   }

   static {
      OPERATING_SYSTEM_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
      OPERATING_SYSTEM_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
      if (OPERATING_SYSTEM_NAME.startsWith("linux")) {
         platform = PlatformEnum.LINUX;
      } else if (!OPERATING_SYSTEM_NAME.startsWith("mac") && !OPERATING_SYSTEM_NAME.startsWith("darwin")) {
         if (OPERATING_SYSTEM_NAME.startsWith("windows")) {
            platform = PlatformEnum.WINDOWS;
         } else {
            platform = PlatformEnum.UNKNOWN;
         }
      } else {
         platform = PlatformEnum.MACOSX;
      }

      arch = normalizeArch(OPERATING_SYSTEM_ARCH);
   }
}
