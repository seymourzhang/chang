package com.chang.util.jwt;

import cn.hutool.jwt.signers.JWTSigner;

public class SignerRSAPair {
   private JWTSigner privateSigner;
   private JWTSigner publicSigner;

   public JWTSigner getPrivateSigner() {
      return this.privateSigner;
   }

   public JWTSigner getPublicSigner() {
      return this.publicSigner;
   }

   public void setPrivateSigner(JWTSigner privateSigner) {
      this.privateSigner = privateSigner;
   }

   public void setPublicSigner(JWTSigner publicSigner) {
      this.publicSigner = publicSigner;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SignerRSAPair)) {
         return false;
      } else {
         SignerRSAPair other = (SignerRSAPair)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$privateSigner = this.getPrivateSigner();
            Object other$privateSigner = other.getPrivateSigner();
            if (this$privateSigner == null) {
               if (other$privateSigner != null) {
                  return false;
               }
            } else if (!this$privateSigner.equals(other$privateSigner)) {
               return false;
            }

            Object this$publicSigner = this.getPublicSigner();
            Object other$publicSigner = other.getPublicSigner();
            if (this$publicSigner == null) {
               if (other$publicSigner != null) {
                  return false;
               }
            } else if (!this$publicSigner.equals(other$publicSigner)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SignerRSAPair;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $privateSigner = this.getPrivateSigner();
      result = result * 59 + ($privateSigner == null ? 43 : $privateSigner.hashCode());
      Object $publicSigner = this.getPublicSigner();
      result = result * 59 + ($publicSigner == null ? 43 : $publicSigner.hashCode());
      return result;
   }

   public String toString() {
      return "SignerRSAPair(privateSigner=" + this.getPrivateSigner() + ", publicSigner=" + this.getPublicSigner() + ")";
   }
}
