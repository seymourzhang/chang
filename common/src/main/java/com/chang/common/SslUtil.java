package com.chang.common;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class SslUtil {
   public static SSLSocketFactory getSingleSocketFactory(String caCrtFile) throws Exception {
      Security.addProvider(new BouncyCastleProvider());
      KeyStore caKs = loadCAKeyStore(caCrtFile);
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init(caKs);
      SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
      sslContext.init((KeyManager[])null, tmf.getTrustManagers(), (SecureRandom)null);
      return sslContext.getSocketFactory();
   }

   public static SSLSocketFactory getSocketFactory(String caCrtFile, String crtFile, String keyFile, String password) throws Exception {
      Security.addProvider(new BouncyCastleProvider());
      KeyStore caKs = loadCAKeyStore(caCrtFile);
      KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
      ks.load((InputStream)null, (char[])null);
      FileInputStream fis = new FileInputStream(crtFile);
      Throwable var8 = null;

      Certificate[] chain;
      try {
         CertificateFactory cf = CertificateFactory.getInstance("X.509");
         Collection<? extends Certificate> certs = cf.generateCertificates(fis);
         chain = (Certificate[])certs.toArray(new Certificate[0]);
      } catch (Throwable var33) {
         var8 = var33;
         throw var33;
      } finally {
         if (fis != null) {
            if (var8 != null) {
               try {
                  fis.close();
               } catch (Throwable var30) {
                  var8.addSuppressed(var30);
               }
            } else {
               fis.close();
            }
         }

      }

      PEMParser pemParser = new PEMParser(new FileReader(keyFile));
      var8 = null;

      try {
         Object object = pemParser.readObject();
         JcaPEMKeyConverter converter = (new JcaPEMKeyConverter()).setProvider("BC");
         KeyPair key = converter.getKeyPair((PEMKeyPair)object);
         ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), chain);
      } catch (Throwable var32) {
         var8 = var32;
         throw var32;
      } finally {
         if (pemParser != null) {
            if (var8 != null) {
               try {
                  pemParser.close();
               } catch (Throwable var31) {
                  var8.addSuppressed(var31);
               }
            } else {
               pemParser.close();
            }
         }

      }

      TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
      tmf.init(caKs);
      KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      kmf.init(ks, password.toCharArray());
      SSLContext context = SSLContext.getInstance("TLSv1.2");
      context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), (SecureRandom)null);
      return context.getSocketFactory();
   }

   private static KeyStore loadCAKeyStore(String caCrtFile) throws Exception {
      KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
      caKs.load((InputStream)null, (char[])null);
      FileInputStream fis = new FileInputStream(caCrtFile);
      Throwable var3 = null;

      try {
         CertificateFactory cf = CertificateFactory.getInstance("X.509");
         Collection<? extends Certificate> caCerts = cf.generateCertificates(fis);
         int certIndex = 1;
         Iterator var7 = caCerts.iterator();

         while(var7.hasNext()) {
            Certificate caCert = (Certificate)var7.next();
            String alias = "ca-certificate-" + certIndex++;
            caKs.setCertificateEntry(alias, caCert);
         }
      } catch (Throwable var17) {
         var3 = var17;
         throw var17;
      } finally {
         if (fis != null) {
            if (var3 != null) {
               try {
                  fis.close();
               } catch (Throwable var16) {
                  var3.addSuppressed(var16);
               }
            } else {
               fis.close();
            }
         }

      }

      return caKs;
   }
}
