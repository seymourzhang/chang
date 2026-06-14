package com.chang.common.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {
   public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
      Properties propertiesFromYaml = this.loadYamlIntoProperties(resource);
      String sourceName = name != null ? name : resource.getResource().getFilename();
      return new PropertiesPropertySource(sourceName, propertiesFromYaml);
   }

   private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
      try {
         YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
         factory.setResources(new Resource[]{resource.getResource()});
         factory.afterPropertiesSet();
         return factory.getObject();
      } catch (IllegalStateException var4) {
         Throwable cause = var4.getCause();
         if (cause instanceof FileNotFoundException) {
            throw (FileNotFoundException)var4.getCause();
         } else {
            throw var4;
         }
      }
   }
}
