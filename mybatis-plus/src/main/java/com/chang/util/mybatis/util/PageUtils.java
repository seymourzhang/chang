package com.chang.util.mybatis.util;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtils {
   public static <P, V> Page<V> Page2Page(Page<P> page, Function<P, V> function) {
      List<P> recordsP = page.getRecords();
      List<V> recordsV = (List)recordsP.stream().map(function).collect(Collectors.toList());
      Page<V> backPage = new Page();
      backPage.setSize(page.getSize()).setCurrent(page.getCurrent()).setPages(page.getPages()).setTotal(page.getTotal()).setRecords(recordsV);
      return backPage;
   }

   public static <P, V> PageInfo<?> OldPage(Page<V> page, Function<V, P> function) {
      if (ObjectUtil.isNotNull(function)) {
         List<P> recordsP = (List)page.getRecords().stream().map(function).collect(Collectors.toList());
         PageInfo<P> pageInfo = new PageInfo();
         pageInfo.setList(recordsP);
         pageInfo.setTotal(page.getTotal());
         return pageInfo;
      } else {
         PageInfo<V> pageInfo = new PageInfo();
         pageInfo.setList(page.getRecords());
         pageInfo.setTotal(page.getTotal());
         return pageInfo;
      }
   }
}
