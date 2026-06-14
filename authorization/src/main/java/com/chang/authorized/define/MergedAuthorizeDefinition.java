package org.hswebframework.web.authorization.basic.define;

import java.io.Serializable;
import org.hswebframework.web.authorization.define.DimensionsDefinition;
import org.hswebframework.web.authorization.define.ResourcesDefinition;

public class MergedAuthorizeDefinition implements Serializable {
   private ResourcesDefinition resources = new ResourcesDefinition();
   private DimensionsDefinition dimensions = new DimensionsDefinition();

   public ResourcesDefinition getResources() {
      return this.resources;
   }

   public DimensionsDefinition getDimensions() {
      return this.dimensions;
   }

   public void setResources(ResourcesDefinition resources) {
      this.resources = resources;
   }

   public void setDimensions(DimensionsDefinition dimensions) {
      this.dimensions = dimensions;
   }
}
