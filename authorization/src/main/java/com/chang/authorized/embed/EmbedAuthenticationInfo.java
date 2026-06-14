package org.hswebframework.web.authorization.basic.embed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.Permission;
import org.hswebframework.web.authorization.builder.DataAccessConfigBuilderFactory;
import org.hswebframework.web.authorization.simple.SimpleAuthentication;
import org.hswebframework.web.authorization.simple.SimplePermission;
import org.hswebframework.web.authorization.simple.SimpleRole;
import org.hswebframework.web.authorization.simple.SimpleUser;

public class EmbedAuthenticationInfo {
   private String id;
   private String name;
   private String username;
   private String type;
   private String password;
   private List<SimpleRole> roles = new ArrayList();
   private List<PermissionInfo> permissions = new ArrayList();
   private Map<String, List<String>> permissionsSimple = new HashMap();

   public Authentication toAuthentication(DataAccessConfigBuilderFactory factory) {
      SimpleAuthentication authentication = new SimpleAuthentication();
      SimpleUser user = new SimpleUser();
      user.setId(this.id);
      user.setName(this.name);
      user.setUsername(this.username);
      user.setUserType(this.type);
      authentication.setUser(user);
      authentication.getDimensions().addAll(this.roles);
      List<Permission> permissionList = new ArrayList();
      permissionList.addAll((Collection)this.permissions.stream().map((info) -> {
         SimplePermission permission = new SimplePermission();
         permission.setId(info.getId());
         permission.setName(info.getName());
         permission.setActions(info.getActions());
         permission.setDataAccesses((Set)info.getDataAccesses().stream().map((conf) -> {
            return factory.create().fromMap(conf).build();
         }).collect(Collectors.toSet()));
         return permission;
      }).collect(Collectors.toList()));
      permissionList.addAll((Collection)this.permissionsSimple.entrySet().stream().map((entry) -> {
         SimplePermission permission = new SimplePermission();
         permission.setId((String)entry.getKey());
         permission.setActions(new HashSet((Collection)entry.getValue()));
         return permission;
      }).collect(Collectors.toList()));
      authentication.setPermissions(permissionList);
      return authentication;
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getUsername() {
      return this.username;
   }

   public String getType() {
      return this.type;
   }

   public String getPassword() {
      return this.password;
   }

   public List<SimpleRole> getRoles() {
      return this.roles;
   }

   public List<PermissionInfo> getPermissions() {
      return this.permissions;
   }

   public Map<String, List<String>> getPermissionsSimple() {
      return this.permissionsSimple;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setRoles(List<SimpleRole> roles) {
      this.roles = roles;
   }

   public void setPermissions(List<PermissionInfo> permissions) {
      this.permissions = permissions;
   }

   public void setPermissionsSimple(Map<String, List<String>> permissionsSimple) {
      this.permissionsSimple = permissionsSimple;
   }

   public static class PermissionInfo {
      private String id;
      private String name;
      private Set<String> actions = new HashSet();
      private List<Map<String, Object>> dataAccesses = new ArrayList();

      public String getId() {
         return this.id;
      }

      public String getName() {
         return this.name;
      }

      public Set<String> getActions() {
         return this.actions;
      }

      public List<Map<String, Object>> getDataAccesses() {
         return this.dataAccesses;
      }

      public void setId(String id) {
         this.id = id;
      }

      public void setName(String name) {
         this.name = name;
      }

      public void setActions(Set<String> actions) {
         this.actions = actions;
      }

      public void setDataAccesses(List<Map<String, Object>> dataAccesses) {
         this.dataAccesses = dataAccesses;
      }
   }
}
