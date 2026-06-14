package com.chang.common.listvalid;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;

public class ValidableList<E> {
   private @Valid List<E> list;

   public ValidableList() {
      this.list = new ArrayList();
   }

   public ValidableList(List<E> list) {
      this.list = list;
   }

   public List<E> getList() {
      return this.list;
   }
}
