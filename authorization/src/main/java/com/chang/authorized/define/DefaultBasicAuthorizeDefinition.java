package org.hswebframework.web.authorization.basic.define;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.authorization.annotation.DataAccess;
import org.hswebframework.web.authorization.annotation.DataAccessType;
import org.hswebframework.web.authorization.annotation.Dimension;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.authorization.annotation.ResourceAction;
import org.hswebframework.web.authorization.define.AopAuthorizeDefinition;
import org.hswebframework.web.authorization.define.DataAccessTypeDefinition;
import org.hswebframework.web.authorization.define.DimensionDefinition;
import org.hswebframework.web.authorization.define.DimensionsDefinition;
import org.hswebframework.web.authorization.define.Phased;
import org.hswebframework.web.authorization.define.ResourceActionDefinition;
import org.hswebframework.web.authorization.define.ResourceDefinition;
import org.hswebframework.web.authorization.define.ResourcesDefinition;
import org.springframework.util.StringUtils;

public class DefaultBasicAuthorizeDefinition implements AopAuthorizeDefinition {
   @JsonIgnore
   private Class<?> targetClass;
   @JsonIgnore
   private Method targetMethod;
   private ResourcesDefinition resources = new ResourcesDefinition();
   private DimensionsDefinition dimensions = new DimensionsDefinition();
   private String message = "权限不足,拒绝访问";
   private Phased phased;
   private static final Set<Class<? extends Annotation>> types = new HashSet(Arrays.asList(Authorize.class, DataAccess.class, Dimension.class, Resource.class, ResourceAction.class, DataAccessType.class));

   public boolean isEmpty() {
      return false;
   }

   public static AopAuthorizeDefinition from(Class<?> targetClass, Method method) {
      AopAuthorizeDefinitionParser parser = new AopAuthorizeDefinitionParser(targetClass, method);
      return parser.parse();
   }

   public void putAnnotation(Authorize ann) {
      if (!ann.merge()) {
         this.getResources().getResources().clear();
         this.getDimensions().getDimensions().clear();
      }

      this.getResources().setPhased(ann.phased());
      Resource[] var2 = ann.resources();
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         Resource resource = var2[var4];
         this.putAnnotation(resource);
      }

      Dimension[] var6 = ann.dimension();
      var3 = var6.length;

      for(var4 = 0; var4 < var3; ++var4) {
         Dimension dimension = var6[var4];
         this.putAnnotation(dimension);
      }

   }

   public void putAnnotation(Dimension ann) {
      if (ann.ignore()) {
         this.getDimensions().getDimensions().clear();
      } else {
         DimensionDefinition definition = new DimensionDefinition();
         definition.setTypeId(ann.type());
         definition.setDimensionId(new HashSet(Arrays.asList(ann.id())));
         definition.setLogical(ann.logical());
         this.getDimensions().addDimension(definition);
      }
   }

   public void putAnnotation(Resource ann) {
      ResourceDefinition resource = new ResourceDefinition();
      resource.setId(ann.id());
      resource.setName(ann.name());
      resource.setLogical(ann.logical());
      resource.setPhased(ann.phased());
      resource.setDescription(String.join("\n", ann.description()));
      ResourceAction[] var3 = ann.actions();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ResourceAction action = var3[var5];
         this.putAnnotation(resource, action);
      }

      resource.setGroup(new ArrayList(Arrays.asList(ann.group())));
      this.resources.addResource(resource, ann.merge());
   }

   public ResourceActionDefinition putAnnotation(ResourceDefinition definition, ResourceAction ann) {
      ResourceActionDefinition actionDefinition = new ResourceActionDefinition();
      actionDefinition.setId(ann.id());
      actionDefinition.setName(ann.name());
      actionDefinition.setDescription(String.join("\n", ann.description()));
      DataAccess[] var4 = ann.dataAccess();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         DataAccess dataAccess = var4[var6];
         this.putAnnotation(actionDefinition, dataAccess);
      }

      definition.addAction(actionDefinition);
      return actionDefinition;
   }

   public void putAnnotation(ResourceActionDefinition definition, DataAccess ann) {
      if (!ann.ignore()) {
         DataAccessTypeDefinition typeDefinition = new DataAccessTypeDefinition();
         DataAccessType[] var4 = ann.type();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            DataAccessType dataAccessType = var4[var6];
            if (!dataAccessType.ignore()) {
               typeDefinition.setId(dataAccessType.id());
               typeDefinition.setName(dataAccessType.name());
               typeDefinition.setController(dataAccessType.controller());
               typeDefinition.setConfiguration(dataAccessType.configuration());
               typeDefinition.setDescription(String.join("\n", dataAccessType.description()));
            }
         }

         if (!StringUtils.isEmpty(typeDefinition.getId())) {
            definition.getDataAccess().getDataAccessTypes().add(typeDefinition);
         }
      }
   }

   public void putAnnotation(ResourceActionDefinition definition, DataAccessType dataAccessType) {
      if (!dataAccessType.ignore()) {
         DataAccessTypeDefinition typeDefinition = new DataAccessTypeDefinition();
         typeDefinition.setId(dataAccessType.id());
         typeDefinition.setName(dataAccessType.name());
         typeDefinition.setController(dataAccessType.controller());
         typeDefinition.setConfiguration(dataAccessType.configuration());
         typeDefinition.setDescription(String.join("\n", dataAccessType.description()));
         definition.getDataAccess().getDataAccessTypes().add(typeDefinition);
      }
   }

   public Class<?> getTargetClass() {
      return this.targetClass;
   }

   public Method getTargetMethod() {
      return this.targetMethod;
   }

   public ResourcesDefinition getResources() {
      return this.resources;
   }

   public DimensionsDefinition getDimensions() {
      return this.dimensions;
   }

   public String getMessage() {
      return this.message;
   }

   public Phased getPhased() {
      return this.phased;
   }

   public void setTargetClass(Class<?> targetClass) {
      this.targetClass = targetClass;
   }

   public void setTargetMethod(Method targetMethod) {
      this.targetMethod = targetMethod;
   }

   public void setResources(ResourcesDefinition resources) {
      this.resources = resources;
   }

   public void setDimensions(DimensionsDefinition dimensions) {
      this.dimensions = dimensions;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public void setPhased(Phased phased) {
      this.phased = phased;
   }

   public DefaultBasicAuthorizeDefinition() {
   }

   public DefaultBasicAuthorizeDefinition(Class<?> targetClass, Method targetMethod, ResourcesDefinition resources, DimensionsDefinition dimensions, String message, Phased phased) {
      this.targetClass = targetClass;
      this.targetMethod = targetMethod;
      this.resources = resources;
      this.dimensions = dimensions;
      this.message = message;
      this.phased = phased;
   }

   public String toString() {
      return "DefaultBasicAuthorizeDefinition(targetClass=" + this.getTargetClass() + ", targetMethod=" + this.getTargetMethod() + ", resources=" + this.getResources() + ", dimensions=" + this.getDimensions() + ", message=" + this.getMessage() + ", phased=" + this.getPhased() + ")";
   }
}
