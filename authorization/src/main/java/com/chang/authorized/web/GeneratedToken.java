package org.hswebframework.web.authorization.basic.web;

import java.io.Serializable;
import java.util.Map;

public interface GeneratedToken extends Serializable {
   Map<String, Object> getResponse();

   String getToken();

   String getType();

   long getTimeout();
}
