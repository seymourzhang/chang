package com.chang.util.mybatis.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import com.chang.common.CommUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseMyBitsPlusServiceImpl<M extends BaseMyBitsPlusMapper<T>, T> extends MppServiceImpl<M, T> {
   public Collection<T> saveBatchPost(Collection<T> entityList) {
      return entityList;
   }

   @Transactional(
      rollbackFor = {Exception.class}
   )
   public boolean saveBatch(Collection<T> entityList, int batchSize) {
      Collection<T> tCollection = this.saveBatchPost(entityList);
      return super.saveBatch(tCollection, batchSize);
   }

   public T saveOrUpdatePre(T entity) {
      return entity;
   }

   @Transactional(
      rollbackFor = {Exception.class}
   )
   public boolean saveOrUpdate(T entity) {
      T t = this.saveOrUpdatePre(entity);
      return super.saveOrUpdate(t);
   }

   public Object selectByIdPost(T entity) {
      return entity;
   }

   @Transactional(
      rollbackFor = {Exception.class}
   )
   public Object selectById(Serializable id) {
      T t = (T) ((BaseMyBitsPlusMapper)this.baseMapper).selectById(id);
      return this.selectByIdPost(t);
   }

   public void removeByIdsPre(Collection<?> list) {
   }

   @Transactional(
      rollbackFor = {Exception.class}
   )
   public boolean removeByIds(Collection<?> list) {
      this.removeByIdsPre(list);
      List<Long> ids = (List)list.stream().map((x) -> {
         return Long.parseLong((String)x);
      }).collect(Collectors.toList());
      return super.removeByIds(ids);
   }

   public void selectByMapForPagePre(final QueryWrapper<T> queryWrapper, Map<String, Object> entity) {
      Iterator var3 = entity.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, Object> entry = (Map.Entry)var3.next();
         queryWrapper.like(CommUtils.humpToLine((String)entry.getKey()), entry.getValue());
      }

   }

   public Page<?> selectByMapForPagePost(Page<T> tPage) {
      return tPage;
   }

   @Transactional(
      rollbackFor = {Exception.class}
   )
   public Page<?> selectByMapForPage(T t, String orderBy, String orderType, Integer page, Integer limit) {
      Map<String, Object> entity = BeanUtil.beanToMap(t, (String[])null);
      entity = MapUtil.removeNullValue(entity);
      QueryWrapper<T> queryWrapper = new QueryWrapper();
      this.selectByMapForPagePre(queryWrapper, entity);
      if (StringUtils.isNoneBlank(new CharSequence[]{orderType}) && StringUtils.isNoneBlank(new CharSequence[]{orderBy})) {
         if (Objects.equals(orderType, "asc")) {
            queryWrapper.orderByAsc(CommUtils.humpToLine(orderBy));
         } else {
            queryWrapper.orderByDesc(CommUtils.humpToLine(orderBy));
         }
      }

      Page<T> pageObject = null;
      if (!ObjectUtil.isNull(page) && !ObjectUtil.isNull(limit)) {
         pageObject = new Page((long)page, (long)limit);
      } else {
         pageObject = new Page(1L, 10L);
      }

      Page<T> tPage = (Page)((BaseMyBitsPlusMapper)this.baseMapper).selectPage(pageObject, queryWrapper);
      return this.selectByMapForPagePost(tPage);
   }

   public void selectListAllByMapPre(final QueryWrapper<T> queryWrapper, Map<String, Object> entity) {
      Iterator var3 = entity.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, Object> entry = (Map.Entry)var3.next();
         queryWrapper.like(CommUtils.humpToLine((String)entry.getKey()), entry.getValue());
      }

   }

   public List<T> selectByMapPost(List<T> result) {
      return result;
   }

   @Transactional(
      rollbackFor = {Exception.class}
   )
   public List<T> getListAllByMap(T t, String orderBy, String orderType) {
      Map<String, Object> entity = BeanUtil.beanToMap(t, (String[])null);
      entity = MapUtil.removeNullValue(entity);
      QueryWrapper<T> queryWrapper = new QueryWrapper();
      this.selectListAllByMapPre(queryWrapper, entity);
      if (StringUtils.isNoneBlank(new CharSequence[]{orderType}) && StringUtils.isNoneBlank(new CharSequence[]{orderBy})) {
         if (Objects.equals(orderType, "asc")) {
            queryWrapper.orderByAsc(CommUtils.humpToLine(orderBy));
         } else {
            queryWrapper.orderByDesc(CommUtils.humpToLine(orderBy));
         }
      }

      List<T> result = ((BaseMyBitsPlusMapper)this.baseMapper).selectList(queryWrapper);
      return this.selectByMapPost(result);
   }

   public QueryWrapper<T> getQueryWrapper() {
      QueryWrapper<T> queryWrapper = new QueryWrapper();
      return queryWrapper;
   }

   public UpdateWrapper<T> getUpdateWrapper() {
      UpdateWrapper<T> updateWrapper = new UpdateWrapper();
      return updateWrapper;
   }

   public LambdaQueryWrapper<T> getLambdaQueryWrapper() {
      return Wrappers.lambdaQuery();
   }

   public LambdaUpdateWrapper<T> getLambdaUpdateWrapper() {
      return Wrappers.lambdaUpdate();
   }

   public List<T> selectByWrapper(QueryWrapper<T> queryWrapper) {
      return ((BaseMyBitsPlusMapper)this.baseMapper).selectList(queryWrapper);
   }

   @Transactional(
      readOnly = true
   )
   public Page<T> getPage(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer limit, Consumer<T> consumer) {
      Page<T> pageObject = new Page((long)page, (long)limit);
      Page<T> tPage = (Page)((BaseMyBitsPlusMapper)this.baseMapper).selectPage(pageObject, queryWrapper);
      if (ObjectUtil.isNotNull(consumer)) {
         tPage.getRecords().forEach(consumer);
      }

      return tPage;
   }

   @Transactional(
      readOnly = true
   )
   public List<T> selectListAll() {
      return ((BaseMyBitsPlusMapper)this.baseMapper).selectAll();
   }
}
