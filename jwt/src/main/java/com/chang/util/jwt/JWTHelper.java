package com.chang.util.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import org.joda.time.DateTime;

public class JWTHelper {
   private static final Long ADVANCE_EXPIRE_TIME = 300000L;
   private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();
   private static String claimKey = "JWT_Data_Key";

   public static <T> String generateToken(String name, T obj, String priKeyPath, int expire) throws Exception {
      String compactJws = Jwts.builder().setSubject(name).claim(claimKey, obj).setExpiration(DateTime.now().plusSeconds(expire).toDate()).signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKeyPath)).compact();
      return compactJws;
   }

   public static <T> String generateToken(String name, T obj, byte[] priKey, int expire) throws Exception {
      String compactJws = Jwts.builder().setSubject(name).claim(claimKey, obj).setExpiration(DateTime.now().plusSeconds(expire).toDate()).signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKey)).compact();
      return compactJws;
   }

   public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKeyPath)).parseClaimsJws(token);
      return claimsJws;
   }

   public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKey)).parseClaimsJws(token);
      return claimsJws;
   }

   public static <T> T getInfoFromToken(String token, String pubKeyPath, Class<T> clazz) throws Exception {
      Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
      Claims body = (Claims)claimsJws.getBody();
      Map map = (Map)body.get(claimKey);
      return BeanUtil.mapToBean(map, clazz, false, CopyOptions.create());
   }

   public static <T> T getInfoFromToken(String token, byte[] pubKey, Class<T> clazz) throws Exception {
      Jws<Claims> claimsJws = parserToken(token, pubKey);
      Claims body = (Claims)claimsJws.getBody();
      Map map = (Map)body.get(claimKey);
      if (clazz.isInterface()) {
         throw new RuntimeException("class is Interface,must is bean");
      } else {
         return BeanUtil.mapToBean(map, clazz, false, CopyOptions.create());
      }
   }

   public static Date getExpirationDateFromToken(String token, byte[] pubKey) throws Exception {
      Jws<Claims> claimsJws = parserToken(token, pubKey);
      Claims body = (Claims)claimsJws.getBody();
      return body.getExpiration();
   }

   public static Date getExpirationDateFromToken(String token, String pubKeyPath) throws Exception {
      Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
      Claims body = (Claims)claimsJws.getBody();
      return body.getExpiration();
   }

   public static Boolean checkToken(String token, String pubKeyPath) throws Exception {
      Long expireTime = getExpirationDateFromToken(token, pubKeyPath).getTime();
      Long diff = expireTime - System.currentTimeMillis();
      return diff < ADVANCE_EXPIRE_TIME ? false : true;
   }

   public static Boolean checkToken(String token, byte[] pubKey) throws Exception {
      Long expireTime = getExpirationDateFromToken(token, pubKey).getTime();
      Long diff = expireTime - System.currentTimeMillis();
      return diff < ADVANCE_EXPIRE_TIME ? false : true;
   }
}
