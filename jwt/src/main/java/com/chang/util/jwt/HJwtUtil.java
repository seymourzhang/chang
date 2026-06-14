package com.chang.util.jwt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import org.joda.time.DateTime;

public class HJwtUtil {
   public static PublicKey getPublicKey(byte[] publicKey) {
      return KeyUtil.generateRSAPublicKey(publicKey);
   }

   public static PrivateKey getPrivateKey(byte[] privateKey) {
      return KeyUtil.generateRSAPrivateKey(privateKey);
   }

   public static KeyPair getRSAKeyPair() {
      return SecureUtil.generateKeyPair("RSA");
   }

   public static SignerRSAPair getRSASignerPair(KeyPair rsaKeyPair) {
      JWTSigner privateSigner = JWTSignerUtil.rs256(rsaKeyPair.getPrivate());
      JWTSigner publicSigner = JWTSignerUtil.rs256(rsaKeyPair.getPublic());
      SignerRSAPair signerRSAPair = new SignerRSAPair();
      signerRSAPair.setPublicSigner(publicSigner);
      signerRSAPair.setPrivateSigner(privateSigner);
      return signerRSAPair;
   }

   public static SignerRSAPair getRSASignerPair(PublicKey publicKey, PrivateKey privateKey) {
      JWTSigner privateSigner = JWTSignerUtil.rs256(privateKey);
      JWTSigner publicSigner = JWTSignerUtil.rs256(publicKey);
      SignerRSAPair signerRSAPair = new SignerRSAPair();
      signerRSAPair.setPublicSigner(publicSigner);
      signerRSAPair.setPrivateSigner(privateSigner);
      return signerRSAPair;
   }

   public static JWTSigner getSigner(Key key) {
      return JWTSignerUtil.rs256(key);
   }

   public static String sign(Map<String, ?> payloads, JWTSigner signer, int seconds) {
      return ((JWT)JWT.create().addPayloads(payloads).setSigner(signer).setExpiresAt(DateTime.now().plusSeconds(seconds).toDate())).sign();
   }

   public static Object parse(String token, String name) {
      return cn.hutool.jwt.JWTUtil.parseToken(token).getPayload().getClaim(name);
   }

   public static boolean validateAlgorithm(String token, JWTSigner signer) {
      try {
         JWTValidator.of(token).validateAlgorithm(signer);
         return true;
      } catch (ValidateException var3) {
         return false;
      }
   }

   public static boolean validateDate(String token) {
      try {
         JWTValidator.of(token).validateDate(DateUtil.date());
         return true;
      } catch (ValidateException var2) {
         return false;
      }
   }

   public static String createToken(Map<String, Object> payload, byte[] key) {
      return cn.hutool.jwt.JWTUtil.createToken(payload, key);
   }

   public static boolean verify(String token, byte[] key) {
      return cn.hutool.jwt.JWTUtil.verify(token, key);
   }
}
