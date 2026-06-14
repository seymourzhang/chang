package com.chang.common.exception;

import java.util.List;

public class PageInfoBean<T> {
   private Long totalElements;
   private Integer size;
   private Integer totalPages;
   private List<T> listdata;

   public static <T> PageInfoBeanBuilder<T> builder() {
      return new PageInfoBeanBuilder();
   }

   public Long getTotalElements() {
      return this.totalElements;
   }

   public Integer getSize() {
      return this.size;
   }

   public Integer getTotalPages() {
      return this.totalPages;
   }

   public List<T> getListdata() {
      return this.listdata;
   }

   public void setTotalElements(Long totalElements) {
      this.totalElements = totalElements;
   }

   public void setSize(Integer size) {
      this.size = size;
   }

   public void setTotalPages(Integer totalPages) {
      this.totalPages = totalPages;
   }

   public void setListdata(List<T> listdata) {
      this.listdata = listdata;
   }

   public PageInfoBean(Long totalElements, Integer size, Integer totalPages, List<T> listdata) {
      this.totalElements = totalElements;
      this.size = size;
      this.totalPages = totalPages;
      this.listdata = listdata;
   }

   public PageInfoBean() {
   }

   public static class PageInfoBeanBuilder<T> {
      private Long totalElements;
      private Integer size;
      private Integer totalPages;
      private List<T> listdata;

      PageInfoBeanBuilder() {
      }

      public PageInfoBeanBuilder<T> totalElements(Long totalElements) {
         this.totalElements = totalElements;
         return this;
      }

      public PageInfoBeanBuilder<T> size(Integer size) {
         this.size = size;
         return this;
      }

      public PageInfoBeanBuilder<T> totalPages(Integer totalPages) {
         this.totalPages = totalPages;
         return this;
      }

      public PageInfoBeanBuilder<T> listdata(List<T> listdata) {
         this.listdata = listdata;
         return this;
      }

      public PageInfoBean<T> build() {
         return new PageInfoBean(this.totalElements, this.size, this.totalPages, this.listdata);
      }

      public String toString() {
         return "PageInfoBean.PageInfoBeanBuilder(totalElements=" + this.totalElements + ", size=" + this.size + ", totalPages=" + this.totalPages + ", listdata=" + this.listdata + ")";
      }
   }
}
