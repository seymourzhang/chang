package com.chang.util.mybatis.base;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chang.util.mybatis.config.MybatisPlusProperties;
import com.chang.util.mybatis.util.PageInfo;
import com.chang.GetException;
import com.chang.common.exception.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseMyBitsPlusController<E extends Serializable, M extends BaseMyBitsPlusMapper<E>, Biz extends BaseMyBitsPlusServiceImpl<M, E>> {
   @Autowired
   private MybatisPlusProperties mybatisPlusProperties;
   @Autowired
   protected Biz baseBiz;

   @ApiOperation(
      value = "[平台]平台默认新增接口",
      notes = "平台默认新增接口",
      httpMethod = "POST"
   )
   @RequestMapping(
      value = {""},
      method = {RequestMethod.POST}
   )
   @GetException
   @ResponseBody
   public Response<E> add(@ApiParam @RequestBody @Valid E entity) {
      this.baseBiz.saveOrUpdate(entity);
      return Response.getInstance().buildSuccess((Object)entity);
   }

   @ApiOperation(
      value = "[平台]平台默认根据id查询实体",
      notes = "平台默认根据id查询实体",
      httpMethod = "GET"
   )
   @RequestMapping(
      value = {"/{id}"},
      method = {RequestMethod.GET}
   )
   @GetException
   @ResponseBody
   public Response<E> get(@ApiParam @PathVariable int id) {
      Object o = this.baseBiz.selectById(id);
      return Response.getInstance().buildSuccess((Object)((Serializable)o));
   }

   @ApiOperation(
      value = "[平台]平台默认根据id更新实体",
      notes = "[平台]根据id更新实体-请注意只更新不为null的字段",
      httpMethod = "PUT"
   )
   @RequestMapping(
      value = {"/{id}"},
      method = {RequestMethod.PUT}
   )
   @GetException
   @ResponseBody
   public Response<E> update(@ApiParam @PathVariable("id") Long id, @ApiParam @RequestBody @Valid E entity) {
      E entity_mod = (E)BeanUtil.setFieldValue(entity, "id", id);
      this.baseBiz.saveOrUpdate(entity_mod);
      return Response.getInstance().buildSuccess();
   }

   @ApiOperation(
      value = "[平台]平台默认-根据ids 批量删除",
      notes = "[平台]平台默认-根据ids 批量删除",
      httpMethod = "DELETE"
   )
   @RequestMapping(
      value = {"/{ids}"},
      method = {RequestMethod.DELETE}
   )
   @GetException
   @ResponseBody
   public Response<E> removes(@ApiParam @PathVariable String ids) {
      this.baseBiz.removeByIds(Arrays.asList(ids.split(",")));
      return Response.getInstance().buildSuccess();
   }

   @ApiOperation(
      value = "[平台]分页模糊查询",
      notes = "[平台]分页模糊查询-分页字段-[page,limit]-排序字段[order]-排序规则[by: asc desc 两种],不传参默认[1,10] 不进行排序",
      httpMethod = "GET"
   )
   @RequestMapping(
      value = {"/page"},
      method = {RequestMethod.GET}
   )
   @GetException
   @ResponseBody
   public Response<?> listAllByParamForPage(@ApiParam E params, @RequestParam(required = false) String orderBy, @RequestParam(required = false) String orderType, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) {
      Page<?> ePageInfo = this.baseBiz.selectByMapForPage(params, orderBy, orderType, page, limit);
      if (this.mybatisPlusProperties.getOldPageInfo()) {
         PageInfo<E> pageInfo = new PageInfo();
         pageInfo.setList((List<E>) ePageInfo.getRecords());
         pageInfo.setTotal(ePageInfo.getTotal());
         return Response.getInstance().buildSuccess((Object)pageInfo);
      } else {
         return Response.getInstance().buildSuccess(ePageInfo);
      }
   }

   @ApiOperation(
      value = "[平台]模糊查询不分页",
      notes = "[平台]模糊查询不分页-排序字段[order]-排序规则[by: asc desc 两种]",
      httpMethod = "GET"
   )
   @RequestMapping(
      value = {"/list/all"},
      method = {RequestMethod.GET}
   )
   @GetException
   @ResponseBody
   public Response<E> listAllByParam(@ApiParam E params, @RequestParam(required = false) String orderBy, @RequestParam(required = false) String orderType) {
      List<?> list = this.baseBiz.getListAllByMap(params, orderBy, orderType);
      return Response.getInstance().buildSuccess(list);
   }
}
