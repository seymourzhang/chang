package org.hswebframework.web.authorization.basic.handler.access;

import java.util.LinkedList;
import java.util.List;
import org.hswebframework.web.authorization.access.DataAccessConfig;
import org.hswebframework.web.authorization.access.DataAccessController;
import org.hswebframework.web.authorization.access.DataAccessHandler;
import org.hswebframework.web.authorization.define.AuthorizingContext;

public final class DefaultDataAccessController implements DataAccessController {
   private DataAccessController parent;
   private List<DataAccessHandler> handlers;

   public DefaultDataAccessController() {
      this((DataAccessController)null);
   }

   public DefaultDataAccessController(DataAccessController parent) {
      this.handlers = new LinkedList();
      if (parent == this) {
         throw new UnsupportedOperationException();
      } else {
         this.parent = parent;
         this.addHandler(new FieldFilterDataAccessHandler()).addHandler(new DimensionDataAccessHandler());
      }
   }

   public boolean doAccess(DataAccessConfig access, AuthorizingContext context) {
      if (this.parent != null) {
         this.parent.doAccess(access, context);
      }

      return this.handlers.stream().filter((handler) -> {
         return handler.isSupport(access);
      }).allMatch((handler) -> {
         return handler.handle(access, context);
      });
   }

   public DefaultDataAccessController addHandler(DataAccessHandler handler) {
      this.handlers.add(handler);
      return this;
   }

   public void setHandlers(List<DataAccessHandler> handlers) {
      this.handlers = handlers;
   }

   public List<DataAccessHandler> getHandlers() {
      return this.handlers;
   }
}
