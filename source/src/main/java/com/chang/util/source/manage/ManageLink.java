package com.chang.util.source.manage;

import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.link.LinkSource;
import com.chang.util.source.link.LinkSourceInterface;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageLink {
   private static final Logger log = LoggerFactory.getLogger(ManageLink.class);
   private static final Map<String, LinkSourceInterface> linkSourceMap = new ConcurrentHashMap();

   public static void addLinkSource(String name, LinkSourceInterface linkSource) {
      if (!linkSourceMap.containsKey(name)) {
         linkSourceMap.put(name, linkSource);
      } else {
         throw new RuntimeException("InputSource already has name: " + name);
      }
   }

   public static <T, R> void addLinkSource(String name, String inputName, String outputName, Function<T, R> function) {
      if (!linkSourceMap.containsKey(name)) {
         InputSource inputSource = ManageSource.getInputSource(inputName);
         OutputSource outputSource = ManageSource.getOutputSource(outputName);
         if (ObjectUtil.isNotNull(inputSource) && ObjectUtil.isNotNull(outputSource)) {
            LinkSource<T, R> linkSource = new LinkSource(inputSource, outputSource, function);
            linkSourceMap.put(name, linkSource);
         }

      } else {
         throw new RuntimeException("InputSource already has name: " + name);
      }
   }

   public static LinkSourceInterface getLink(String name) {
      return (LinkSourceInterface)linkSourceMap.get(name);
   }

   public static void runListLink(List<String> names) throws Exception {
      Iterator var1 = names.iterator();

      while(var1.hasNext()) {
         String name = (String)var1.next();
         if (linkSourceMap.containsKey(name)) {
            ((LinkSourceInterface)linkSourceMap.get(name)).runLink();
         }
      }

   }

   public static void runAllLink() throws Exception {
      Iterator var0 = linkSourceMap.entrySet().iterator();

      while(var0.hasNext()) {
         Map.Entry<String, LinkSourceInterface> link = (Map.Entry)var0.next();
         log.info("run link name: " + (String)link.getKey() + "time: " + System.currentTimeMillis());
         ((LinkSourceInterface)link.getValue()).runLink();
      }

   }
}
