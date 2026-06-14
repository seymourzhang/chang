package com.chang.common.DbField;

import java.util.Objects;

public class Field {
   public static class SysPermissionField {
      public static final String ID = "id";
      public static final String PID = "pid";
      public static final String IDX = "idx";
      public static final String NAME = "name";
      public static final String RESOURCE_TYPE = "resourceType";
      public static final String URL = "url";
      public static final String PERMISSION = "permission";
      public static final String AVAILABLE = "available";
      public static final String VALIDATE_TYPE = "validateType";
      public static final String PARENT_ID = "parentId";

      public static enum ValidateType {
         regex("regex"),
         field("field");

         private String type;

         private ValidateType(String value) {
            this.type = value;
         }

         public String getValue() {
            return this.type;
         }

         public static boolean fromValue(String value) {
            ValidateType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               ValidateType et = var1[var3];
               if (Objects.equals(value, et.getValue())) {
                  return true;
               }
            }

            return false;
         }

         public static String info() {
            StringBuffer valus = new StringBuffer();
            ValidateType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               ValidateType et = var1[var3];
               valus.append(et.getValue() + ",");
            }

            String str = valus.substring(0, valus.length() - 1);
            return "ValidateType value Range : " + str;
         }
      }

      public static enum ResourceType {
         nav("nav"),
         menu("menu"),
         button("button"),
         restapi("restapi"),
         service("service"),
         other("other"),
         globa("globa");

         private String type;

         private ResourceType(String value) {
            this.type = value;
         }

         public String getValue() {
            return this.type;
         }

         public static boolean fromValue(String value) {
            ResourceType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               ResourceType et = var1[var3];
               if (Objects.equals(value, et.getValue())) {
                  return true;
               }
            }

            return false;
         }

         public static String info() {
            StringBuffer valus = new StringBuffer();
            ResourceType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               ResourceType et = var1[var3];
               valus.append(et.getValue() + ",");
            }

            String str = valus.substring(0, valus.length() - 1);
            return "ResourceType value Range : " + str;
         }
      }
   }

   public static class SysRoleField {
      public static final String ID = "id";
      public static final String RID = "rid";
      public static final String ROLE = "role";
      public static final String DESCRIPTION = "description";
      public static final String AVAILABLE = "available";
      public static final String ROLE_TYPE = "roleType";

      public static enum RoleType {
         system("system"),
         user("user");

         private String type;

         private RoleType(String value) {
            this.type = value;
         }

         public String getValue() {
            return this.type;
         }

         public static boolean fromValue(String value) {
            RoleType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               RoleType et = var1[var3];
               if (Objects.equals(value, et.getValue())) {
                  return true;
               }
            }

            return false;
         }

         public static String info() {
            StringBuffer valus = new StringBuffer();
            RoleType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               RoleType et = var1[var3];
               valus.append(et.getValue() + ",");
            }

            String str = valus.substring(0, valus.length() - 1);
            return "RoleType value Range : " + str;
         }
      }
   }

   public static class UserOnLineField {
      public static final String ID = "id";
      public static final String UID = "uid";
      public static final String NAME = "name";
      public static final String SESSION_ID = "sessionId";
      public static final String EQUIPMENT_TYPE = "equipmentType";
      public static final String LOGIN_STATUS = "loginStatus";
      public static final String UP_TIME = "uptime";

      public static enum LoginStatus {
         on("on"),
         off("off");

         private String type;

         private LoginStatus(String value) {
            this.type = value;
         }

         public String getValue() {
            return this.type;
         }

         public static boolean fromValue(String value) {
            LoginStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               LoginStatus et = var1[var3];
               if (Objects.equals(value, et.getValue())) {
                  return true;
               }
            }

            return false;
         }

         public static String info() {
            StringBuffer valus = new StringBuffer();
            LoginStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               LoginStatus et = var1[var3];
               valus.append(et.getValue() + ",");
            }

            String str = valus.substring(0, valus.length() - 1);
            return "LoginStatus value Range : " + str;
         }
      }

      public static enum EquipmentType {
         web("web"),
         device("device"),
         server("server");

         private String type;

         private EquipmentType(String value) {
            this.type = value;
         }

         public String getValue() {
            return this.type;
         }

         public static boolean fromValue(String value) {
            EquipmentType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               EquipmentType et = var1[var3];
               if (Objects.equals(value, et.getValue())) {
                  return true;
               }
            }

            return false;
         }

         public static String info() {
            StringBuffer valus = new StringBuffer();
            EquipmentType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               EquipmentType et = var1[var3];
               valus.append(et.getValue() + ",");
            }

            String str = valus.substring(0, valus.length() - 1);
            return "EquipmentType value Range : " + str;
         }
      }
   }

   public static class UserInfoField {
      public static final String ID = "id";
      public static final String UID = "uid";
      public static final String NAME = "name";
      public static final String PASSWORD = "password";
      public static final String STATE = "state";
      public static final String ACCOUNT_TYPE = "accountType";
      public static final String SUPERADMIN = "superadmin";
      public static final String MOBILE = "mobile";
      public static final String SMS_CODE = "smscode";
      public static final String AUTH_CODE = "authcode";
      public static final String SMS_TYPE = "smstype";

      public static enum SmsType {
         regist("regist"),
         passwd("passwd"),
         login("login");

         private String type;

         private SmsType(String value) {
            this.type = value;
         }

         public String getValue() {
            return this.type;
         }

         public static boolean fromValue(String value) {
            SmsType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               SmsType et = var1[var3];
               if (Objects.equals(value, et.getValue())) {
                  return true;
               }
            }

            return false;
         }

         public static String info() {
            StringBuffer valus = new StringBuffer();
            SmsType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               SmsType et = var1[var3];
               valus.append(et.getValue() + ",");
            }

            String str = valus.substring(0, valus.length() - 1);
            return "SmsType value Range : " + str;
         }
      }

      public static enum State {
         user_ok("1"),
         user_noactive("0"),
         user_lock("2");

         private String type;

         private State(String value) {
            this.type = value;
         }

         public String getValue() {
            return this.type;
         }

         public static boolean fromValue(String value) {
            State[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               State et = var1[var3];
               if (Objects.equals(value, et.getValue())) {
                  return true;
               }
            }

            return false;
         }

         public static String info() {
            StringBuffer valus = new StringBuffer();
            State[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               State et = var1[var3];
               valus.append(et.getValue() + ",");
            }

            String str = valus.substring(0, valus.length() - 1);
            return "State value Range : " + str;
         }
      }

      public static enum Accounttype {
         common("common");

         private String type;

         private Accounttype(String value) {
            this.type = value;
         }

         public String getValue() {
            return this.type;
         }

         public static boolean fromValue(String value) {
            Accounttype[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               Accounttype et = var1[var3];
               if (Objects.equals(value, et.getValue())) {
                  return true;
               }
            }

            return false;
         }

         public static String info() {
            StringBuffer valus = new StringBuffer();
            Accounttype[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               Accounttype et = var1[var3];
               valus.append(et.getValue() + ",");
            }

            String str = valus.substring(0, valus.length() - 1);
            return "Accounttype value Range : " + str;
         }
      }
   }

   public static class ZuulMsgProtocol {
      public static final String UPDATE_ROUTING = "update_routing";
   }
}
