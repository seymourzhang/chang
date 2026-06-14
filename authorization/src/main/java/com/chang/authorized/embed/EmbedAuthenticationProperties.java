package org.hswebframework.web.authorization.basic.embed;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import jakarta.validation.ValidationException;
import org.hswebframework.web.authorization.Authentication;
import org.hswebframework.web.authorization.AuthenticationRequest;
import org.hswebframework.web.authorization.builder.DataAccessConfigBuilderFactory;
import org.hswebframework.web.authorization.simple.PlainTextUsernamePasswordAuthenticationRequest;
import org.hswebframework.web.authorization.simple.builder.SimpleDataAccessConfigBuilderFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(
   prefix = "hsweb.auth"
)
public class EmbedAuthenticationProperties implements InitializingBean {
   private Map<String, Authentication> authentications = new HashMap();
   private Map<String, EmbedAuthenticationInfo> users = new HashMap();
   @Autowired(
      required = false
   )
   private DataAccessConfigBuilderFactory dataAccessConfigBuilderFactory = new SimpleDataAccessConfigBuilderFactory();

   public void afterPropertiesSet() {
      this.users.forEach((id, properties) -> {
         if (StringUtils.isEmpty(properties.getId())) {
            properties.setId(id);
         }

         Iterator var3 = properties.getPermissions().iterator();

         while(var3.hasNext()) {
            EmbedAuthenticationInfo.PermissionInfo permissionInfo = (EmbedAuthenticationInfo.PermissionInfo)var3.next();
            Iterator var5 = permissionInfo.getDataAccesses().iterator();

            while(var5.hasNext()) {
               Map<String, Object> objectMap = (Map)var5.next();
               Iterator var7 = objectMap.entrySet().iterator();

               while(var7.hasNext()) {
                  Map.Entry<String, Object> stringObjectEntry = (Map.Entry)var7.next();
                  if (stringObjectEntry.getValue() instanceof Map) {
                     Map<?, ?> mapVal = (Map)stringObjectEntry.getValue();
                     boolean maybeIsList = mapVal.keySet().stream().allMatch(org.hswebframework.utils.StringUtils::isInt);
                     if (maybeIsList) {
                        stringObjectEntry.setValue(mapVal.values());
                     }
                  }
               }
            }
         }

         this.authentications.put(id, properties.toAuthentication(this.dataAccessConfigBuilderFactory));
      });
   }

   public Authentication authenticate(AuthenticationRequest request) {
      if (request instanceof PlainTextUsernamePasswordAuthenticationRequest) {
         PlainTextUsernamePasswordAuthenticationRequest pwdReq = (PlainTextUsernamePasswordAuthenticationRequest)request;
         Optional var10000 = this.users.values().stream().filter((user) -> {
            return pwdReq.getUsername().equals(user.getUsername()) && pwdReq.getPassword().equals(user.getPassword());
         }).findFirst().map(EmbedAuthenticationInfo::getId);
         Map var10001 = this.authentications;
         var10001.getClass();
         return (Authentication)var10000.map(var10001::get).orElseThrow(() -> {
            return new ValidationException("用户不存在");
         });
      } else {
         throw new UnsupportedOperationException("不支持的授权请求:" + request);
      }
   }

   public Optional<Authentication> getAuthentication(String userId) {
      return Optional.ofNullable(this.authentications.get(userId));
   }

   public Map<String, Authentication> getAuthentications() {
      return this.authentications;
   }

   public DataAccessConfigBuilderFactory getDataAccessConfigBuilderFactory() {
      return this.dataAccessConfigBuilderFactory;
   }

   public void setAuthentications(Map<String, Authentication> authentications) {
      this.authentications = authentications;
   }

   public void setDataAccessConfigBuilderFactory(DataAccessConfigBuilderFactory dataAccessConfigBuilderFactory) {
      this.dataAccessConfigBuilderFactory = dataAccessConfigBuilderFactory;
   }

   public Map<String, EmbedAuthenticationInfo> getUsers() {
      return this.users;
   }

   public void setUsers(Map<String, EmbedAuthenticationInfo> users) {
      this.users = users;
   }
}
