package com.chang.util.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.crypto.asymmetric.RSA;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Map;
import org.joda.time.DateTime;

public class JWTUtil {
   private static RSA rsa = new RSA();
   private static String claimKey = "JWT_Data_Key";

   public static <T> String generateToken(String name, T obj, int expire) throws Exception {
      String compactJws = Jwts.builder().setSubject(name).claim(claimKey, obj).setExpiration(DateTime.now().plusSeconds(expire).toDate()).signWith(SignatureAlgorithm.RS256, rsa.getPrivateKey()).compact();
      return compactJws;
   }

   public static Jws<Claims> parserToken(String token) throws Exception {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsa.getPublicKey()).parseClaimsJws(token);
      return claimsJws;
   }

   public static <T> T getInfoFromToken(String token, Class<T> clazz) throws Exception {
      Jws<Claims> claimsJws = parserToken(token);
      Claims body = (Claims)claimsJws.getBody();
      Map map = (Map)body.get(claimKey);
      return BeanUtil.mapToBean(map, clazz, false, CopyOptions.create());
   }
}
