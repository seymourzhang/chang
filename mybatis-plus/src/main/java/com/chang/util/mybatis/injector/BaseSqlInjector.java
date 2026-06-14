package com.chang.util.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.jeffreyning.mybatisplus.base.MppSqlInjector;
import com.chang.util.mybatis.methods.FetchByStream;
import java.util.ArrayList;
import java.util.List;

public class BaseSqlInjector extends MppSqlInjector {
   private final List<AbstractMethod> methodExList = new ArrayList(100);

   public void addMethod(AbstractMethod abstractMethod) {
      this.methodExList.add(abstractMethod);
   }

   public void clearMethod() {
      this.methodExList.clear();
   }

   public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
      List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
      methodList.add(new FetchByStream("selectStream"));
      methodList.addAll(this.methodExList);
      return methodList;
   }
}
