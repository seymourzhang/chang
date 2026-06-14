package com.chang.util.source.spring.start;


import com.chang.util.source.spring.start.config.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(
   prefix = "wg.data.source"
)
public class SourceProperties {
   private PublicProperties publicConf;
   private Map<String, DbProperties> dbs;
   private Map<String, SqliteProperties> sqliteDbs;
   private Map<String, MqttOutProperties> mqttOutPutSources;
   private Map<String, MqttInProperties> mqttInPutSources;
   private Map<String, MySqlOutSourceProperties> mysqlOutSources;
   private Map<String, MySqlInSourceProperties> mysqlInSources;
   private Map<String, LinkProperties> links;
   private Map<String, KafkaProperties> kafkaOutPutSources;
   private Map<String, KafkaProperties> kafkaInPutSources;
   private Map<String, RabbitMqProperties> rabbitOutPutSources;
   private Map<String, RabbitMqProperties> rabbitInPutSources;
   private Map<String, MongodbInProperties> mongodbInPutSources;
   private Map<String, MongodbOutProperties> mongodbOutPutSources;
   private Map<String, RocketmqInProperties> rocketmqInPutSources;
   private Map<String, RocketmqOutProperties> rocketmqOutPutSources;
   private Map<String, TcpClientProperties> tcpClientOutPutSources;
   private Map<String, TcpClientProperties> tcpClientIntPutSources;
   private Map<String, TcpServerProperties> tcpServerOutPutSources;
   private Map<String, TcpServerProperties> tcpServerInPutSources;
   private Map<String, InputSourceDoWithProperties> doWithInPutSources;
   private Map<String, HttpInProperties> httpInPutSources;
   private Map<String, HttpOutProperties> httpOutPutSources;
   private Map<String, FileInProperties> fileInPutSources;
   private Map<String, FileOutProperties> fileOutPutSources;
   private Map<String, WebSocketServerProperties> webSocketServerSources;
   private Map<String, WebSocketClientProperties> webSocketClientOutPutSources;
   private Map<String, WebSocketClientProperties> webSocketClientInPutSources;
   private BinLogProperties binLogPropertiesInPutSources;
   private Map<String, RedisProperties> redisDbs;
   private Map<String, HbaseInProperties> hbaseInPutSources;
   private Map<String, HbaseOutProperties> hbaseOutPutSources;
   private Map<String, CsvInProperties> csvInPutSources;
   private Map<String, CsvOutProperties> csvOutPutSources;

   public PublicProperties getPublicConf() {
      return this.publicConf;
   }

   public Map<String, DbProperties> getDbs() {
      return this.dbs;
   }

   public Map<String, SqliteProperties> getSqliteDbs() {
      return this.sqliteDbs;
   }

   public Map<String, MqttOutProperties> getMqttOutPutSources() {
      return this.mqttOutPutSources;
   }

   public Map<String, MqttInProperties> getMqttInPutSources() {
      return this.mqttInPutSources;
   }

   public Map<String, MySqlOutSourceProperties> getMysqlOutSources() {
      return this.mysqlOutSources;
   }

   public Map<String, MySqlInSourceProperties> getMysqlInSources() {
      return this.mysqlInSources;
   }

   public Map<String, LinkProperties> getLinks() {
      return this.links;
   }

   public Map<String, KafkaProperties> getKafkaOutPutSources() {
      return this.kafkaOutPutSources;
   }

   public Map<String, KafkaProperties> getKafkaInPutSources() {
      return this.kafkaInPutSources;
   }

   public Map<String, RabbitMqProperties> getRabbitOutPutSources() {
      return this.rabbitOutPutSources;
   }

   public Map<String, RabbitMqProperties> getRabbitInPutSources() {
      return this.rabbitInPutSources;
   }

   public Map<String, MongodbInProperties> getMongodbInPutSources() {
      return this.mongodbInPutSources;
   }

   public Map<String, MongodbOutProperties> getMongodbOutPutSources() {
      return this.mongodbOutPutSources;
   }

   public Map<String, RocketmqInProperties> getRocketmqInPutSources() {
      return this.rocketmqInPutSources;
   }

   public Map<String, RocketmqOutProperties> getRocketmqOutPutSources() {
      return this.rocketmqOutPutSources;
   }

   public Map<String, TcpClientProperties> getTcpClientOutPutSources() {
      return this.tcpClientOutPutSources;
   }

   public Map<String, TcpClientProperties> getTcpClientIntPutSources() {
      return this.tcpClientIntPutSources;
   }

   public Map<String, TcpServerProperties> getTcpServerOutPutSources() {
      return this.tcpServerOutPutSources;
   }

   public Map<String, TcpServerProperties> getTcpServerInPutSources() {
      return this.tcpServerInPutSources;
   }

   public Map<String, InputSourceDoWithProperties> getDoWithInPutSources() {
      return this.doWithInPutSources;
   }

   public Map<String, HttpInProperties> getHttpInPutSources() {
      return this.httpInPutSources;
   }

   public Map<String, HttpOutProperties> getHttpOutPutSources() {
      return this.httpOutPutSources;
   }

   public Map<String, FileInProperties> getFileInPutSources() {
      return this.fileInPutSources;
   }

   public Map<String, FileOutProperties> getFileOutPutSources() {
      return this.fileOutPutSources;
   }

   public Map<String, WebSocketServerProperties> getWebSocketServerSources() {
      return this.webSocketServerSources;
   }

   public Map<String, WebSocketClientProperties> getWebSocketClientOutPutSources() {
      return this.webSocketClientOutPutSources;
   }

   public Map<String, WebSocketClientProperties> getWebSocketClientInPutSources() {
      return this.webSocketClientInPutSources;
   }

   public BinLogProperties getBinLogPropertiesInPutSources() {
      return this.binLogPropertiesInPutSources;
   }

   public Map<String, RedisProperties> getRedisDbs() {
      return this.redisDbs;
   }

   public Map<String, HbaseInProperties> getHbaseInPutSources() {
      return this.hbaseInPutSources;
   }

   public Map<String, HbaseOutProperties> getHbaseOutPutSources() {
      return this.hbaseOutPutSources;
   }

   public Map<String, CsvInProperties> getCsvInPutSources() {
      return this.csvInPutSources;
   }

   public Map<String, CsvOutProperties> getCsvOutPutSources() {
      return this.csvOutPutSources;
   }

   public void setPublicConf(PublicProperties publicConf) {
      this.publicConf = publicConf;
   }

   public void setDbs(Map<String, DbProperties> dbs) {
      this.dbs = dbs;
   }

   public void setSqliteDbs(Map<String, SqliteProperties> sqliteDbs) {
      this.sqliteDbs = sqliteDbs;
   }

   public void setMqttOutPutSources(Map<String, MqttOutProperties> mqttOutPutSources) {
      this.mqttOutPutSources = mqttOutPutSources;
   }

   public void setMqttInPutSources(Map<String, MqttInProperties> mqttInPutSources) {
      this.mqttInPutSources = mqttInPutSources;
   }

   public void setMysqlOutSources(Map<String, MySqlOutSourceProperties> mysqlOutSources) {
      this.mysqlOutSources = mysqlOutSources;
   }

   public void setMysqlInSources(Map<String, MySqlInSourceProperties> mysqlInSources) {
      this.mysqlInSources = mysqlInSources;
   }

   public void setLinks(Map<String, LinkProperties> links) {
      this.links = links;
   }

   public void setKafkaOutPutSources(Map<String, KafkaProperties> kafkaOutPutSources) {
      this.kafkaOutPutSources = kafkaOutPutSources;
   }

   public void setKafkaInPutSources(Map<String, KafkaProperties> kafkaInPutSources) {
      this.kafkaInPutSources = kafkaInPutSources;
   }

   public void setRabbitOutPutSources(Map<String, RabbitMqProperties> rabbitOutPutSources) {
      this.rabbitOutPutSources = rabbitOutPutSources;
   }

   public void setRabbitInPutSources(Map<String, RabbitMqProperties> rabbitInPutSources) {
      this.rabbitInPutSources = rabbitInPutSources;
   }

   public void setMongodbInPutSources(Map<String, MongodbInProperties> mongodbInPutSources) {
      this.mongodbInPutSources = mongodbInPutSources;
   }

   public void setMongodbOutPutSources(Map<String, MongodbOutProperties> mongodbOutPutSources) {
      this.mongodbOutPutSources = mongodbOutPutSources;
   }

   public void setRocketmqInPutSources(Map<String, RocketmqInProperties> rocketmqInPutSources) {
      this.rocketmqInPutSources = rocketmqInPutSources;
   }

   public void setRocketmqOutPutSources(Map<String, RocketmqOutProperties> rocketmqOutPutSources) {
      this.rocketmqOutPutSources = rocketmqOutPutSources;
   }

   public void setTcpClientOutPutSources(Map<String, TcpClientProperties> tcpClientOutPutSources) {
      this.tcpClientOutPutSources = tcpClientOutPutSources;
   }

   public void setTcpClientIntPutSources(Map<String, TcpClientProperties> tcpClientIntPutSources) {
      this.tcpClientIntPutSources = tcpClientIntPutSources;
   }

   public void setTcpServerOutPutSources(Map<String, TcpServerProperties> tcpServerOutPutSources) {
      this.tcpServerOutPutSources = tcpServerOutPutSources;
   }

   public void setTcpServerInPutSources(Map<String, TcpServerProperties> tcpServerInPutSources) {
      this.tcpServerInPutSources = tcpServerInPutSources;
   }

   public void setDoWithInPutSources(Map<String, InputSourceDoWithProperties> doWithInPutSources) {
      this.doWithInPutSources = doWithInPutSources;
   }

   public void setHttpInPutSources(Map<String, HttpInProperties> httpInPutSources) {
      this.httpInPutSources = httpInPutSources;
   }

   public void setHttpOutPutSources(Map<String, HttpOutProperties> httpOutPutSources) {
      this.httpOutPutSources = httpOutPutSources;
   }

   public void setFileInPutSources(Map<String, FileInProperties> fileInPutSources) {
      this.fileInPutSources = fileInPutSources;
   }

   public void setFileOutPutSources(Map<String, FileOutProperties> fileOutPutSources) {
      this.fileOutPutSources = fileOutPutSources;
   }

   public void setWebSocketServerSources(Map<String, WebSocketServerProperties> webSocketServerSources) {
      this.webSocketServerSources = webSocketServerSources;
   }

   public void setWebSocketClientOutPutSources(Map<String, WebSocketClientProperties> webSocketClientOutPutSources) {
      this.webSocketClientOutPutSources = webSocketClientOutPutSources;
   }

   public void setWebSocketClientInPutSources(Map<String, WebSocketClientProperties> webSocketClientInPutSources) {
      this.webSocketClientInPutSources = webSocketClientInPutSources;
   }

   public void setBinLogPropertiesInPutSources(BinLogProperties binLogPropertiesInPutSources) {
      this.binLogPropertiesInPutSources = binLogPropertiesInPutSources;
   }

   public void setRedisDbs(Map<String, RedisProperties> redisDbs) {
      this.redisDbs = redisDbs;
   }

   public void setHbaseInPutSources(Map<String, HbaseInProperties> hbaseInPutSources) {
      this.hbaseInPutSources = hbaseInPutSources;
   }

   public void setHbaseOutPutSources(Map<String, HbaseOutProperties> hbaseOutPutSources) {
      this.hbaseOutPutSources = hbaseOutPutSources;
   }

   public void setCsvInPutSources(Map<String, CsvInProperties> csvInPutSources) {
      this.csvInPutSources = csvInPutSources;
   }

   public void setCsvOutPutSources(Map<String, CsvOutProperties> csvOutPutSources) {
      this.csvOutPutSources = csvOutPutSources;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SourceProperties)) {
         return false;
      } else {
         SourceProperties other = (SourceProperties)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$publicConf = this.getPublicConf();
            Object other$publicConf = other.getPublicConf();
            if (this$publicConf == null) {
               if (other$publicConf != null) {
                  return false;
               }
            } else if (!this$publicConf.equals(other$publicConf)) {
               return false;
            }

            Object this$dbs = this.getDbs();
            Object other$dbs = other.getDbs();
            if (this$dbs == null) {
               if (other$dbs != null) {
                  return false;
               }
            } else if (!this$dbs.equals(other$dbs)) {
               return false;
            }

            Object this$sqliteDbs = this.getSqliteDbs();
            Object other$sqliteDbs = other.getSqliteDbs();
            if (this$sqliteDbs == null) {
               if (other$sqliteDbs != null) {
                  return false;
               }
            } else if (!this$sqliteDbs.equals(other$sqliteDbs)) {
               return false;
            }

            label398: {
               Object this$mqttOutPutSources = this.getMqttOutPutSources();
               Object other$mqttOutPutSources = other.getMqttOutPutSources();
               if (this$mqttOutPutSources == null) {
                  if (other$mqttOutPutSources == null) {
                     break label398;
                  }
               } else if (this$mqttOutPutSources.equals(other$mqttOutPutSources)) {
                  break label398;
               }

               return false;
            }

            label391: {
               Object this$mqttInPutSources = this.getMqttInPutSources();
               Object other$mqttInPutSources = other.getMqttInPutSources();
               if (this$mqttInPutSources == null) {
                  if (other$mqttInPutSources == null) {
                     break label391;
                  }
               } else if (this$mqttInPutSources.equals(other$mqttInPutSources)) {
                  break label391;
               }

               return false;
            }

            Object this$mysqlOutSources = this.getMysqlOutSources();
            Object other$mysqlOutSources = other.getMysqlOutSources();
            if (this$mysqlOutSources == null) {
               if (other$mysqlOutSources != null) {
                  return false;
               }
            } else if (!this$mysqlOutSources.equals(other$mysqlOutSources)) {
               return false;
            }

            label377: {
               Object this$mysqlInSources = this.getMysqlInSources();
               Object other$mysqlInSources = other.getMysqlInSources();
               if (this$mysqlInSources == null) {
                  if (other$mysqlInSources == null) {
                     break label377;
                  }
               } else if (this$mysqlInSources.equals(other$mysqlInSources)) {
                  break label377;
               }

               return false;
            }

            label370: {
               Object this$links = this.getLinks();
               Object other$links = other.getLinks();
               if (this$links == null) {
                  if (other$links == null) {
                     break label370;
                  }
               } else if (this$links.equals(other$links)) {
                  break label370;
               }

               return false;
            }

            Object this$kafkaOutPutSources = this.getKafkaOutPutSources();
            Object other$kafkaOutPutSources = other.getKafkaOutPutSources();
            if (this$kafkaOutPutSources == null) {
               if (other$kafkaOutPutSources != null) {
                  return false;
               }
            } else if (!this$kafkaOutPutSources.equals(other$kafkaOutPutSources)) {
               return false;
            }

            Object this$kafkaInPutSources = this.getKafkaInPutSources();
            Object other$kafkaInPutSources = other.getKafkaInPutSources();
            if (this$kafkaInPutSources == null) {
               if (other$kafkaInPutSources != null) {
                  return false;
               }
            } else if (!this$kafkaInPutSources.equals(other$kafkaInPutSources)) {
               return false;
            }

            label349: {
               Object this$rabbitOutPutSources = this.getRabbitOutPutSources();
               Object other$rabbitOutPutSources = other.getRabbitOutPutSources();
               if (this$rabbitOutPutSources == null) {
                  if (other$rabbitOutPutSources == null) {
                     break label349;
                  }
               } else if (this$rabbitOutPutSources.equals(other$rabbitOutPutSources)) {
                  break label349;
               }

               return false;
            }

            label342: {
               Object this$rabbitInPutSources = this.getRabbitInPutSources();
               Object other$rabbitInPutSources = other.getRabbitInPutSources();
               if (this$rabbitInPutSources == null) {
                  if (other$rabbitInPutSources == null) {
                     break label342;
                  }
               } else if (this$rabbitInPutSources.equals(other$rabbitInPutSources)) {
                  break label342;
               }

               return false;
            }

            Object this$mongodbInPutSources = this.getMongodbInPutSources();
            Object other$mongodbInPutSources = other.getMongodbInPutSources();
            if (this$mongodbInPutSources == null) {
               if (other$mongodbInPutSources != null) {
                  return false;
               }
            } else if (!this$mongodbInPutSources.equals(other$mongodbInPutSources)) {
               return false;
            }

            label328: {
               Object this$mongodbOutPutSources = this.getMongodbOutPutSources();
               Object other$mongodbOutPutSources = other.getMongodbOutPutSources();
               if (this$mongodbOutPutSources == null) {
                  if (other$mongodbOutPutSources == null) {
                     break label328;
                  }
               } else if (this$mongodbOutPutSources.equals(other$mongodbOutPutSources)) {
                  break label328;
               }

               return false;
            }

            Object this$rocketmqInPutSources = this.getRocketmqInPutSources();
            Object other$rocketmqInPutSources = other.getRocketmqInPutSources();
            if (this$rocketmqInPutSources == null) {
               if (other$rocketmqInPutSources != null) {
                  return false;
               }
            } else if (!this$rocketmqInPutSources.equals(other$rocketmqInPutSources)) {
               return false;
            }

            label314: {
               Object this$rocketmqOutPutSources = this.getRocketmqOutPutSources();
               Object other$rocketmqOutPutSources = other.getRocketmqOutPutSources();
               if (this$rocketmqOutPutSources == null) {
                  if (other$rocketmqOutPutSources == null) {
                     break label314;
                  }
               } else if (this$rocketmqOutPutSources.equals(other$rocketmqOutPutSources)) {
                  break label314;
               }

               return false;
            }

            Object this$tcpClientOutPutSources = this.getTcpClientOutPutSources();
            Object other$tcpClientOutPutSources = other.getTcpClientOutPutSources();
            if (this$tcpClientOutPutSources == null) {
               if (other$tcpClientOutPutSources != null) {
                  return false;
               }
            } else if (!this$tcpClientOutPutSources.equals(other$tcpClientOutPutSources)) {
               return false;
            }

            Object this$tcpClientIntPutSources = this.getTcpClientIntPutSources();
            Object other$tcpClientIntPutSources = other.getTcpClientIntPutSources();
            if (this$tcpClientIntPutSources == null) {
               if (other$tcpClientIntPutSources != null) {
                  return false;
               }
            } else if (!this$tcpClientIntPutSources.equals(other$tcpClientIntPutSources)) {
               return false;
            }

            Object this$tcpServerOutPutSources = this.getTcpServerOutPutSources();
            Object other$tcpServerOutPutSources = other.getTcpServerOutPutSources();
            if (this$tcpServerOutPutSources == null) {
               if (other$tcpServerOutPutSources != null) {
                  return false;
               }
            } else if (!this$tcpServerOutPutSources.equals(other$tcpServerOutPutSources)) {
               return false;
            }

            label286: {
               Object this$tcpServerInPutSources = this.getTcpServerInPutSources();
               Object other$tcpServerInPutSources = other.getTcpServerInPutSources();
               if (this$tcpServerInPutSources == null) {
                  if (other$tcpServerInPutSources == null) {
                     break label286;
                  }
               } else if (this$tcpServerInPutSources.equals(other$tcpServerInPutSources)) {
                  break label286;
               }

               return false;
            }

            label279: {
               Object this$doWithInPutSources = this.getDoWithInPutSources();
               Object other$doWithInPutSources = other.getDoWithInPutSources();
               if (this$doWithInPutSources == null) {
                  if (other$doWithInPutSources == null) {
                     break label279;
                  }
               } else if (this$doWithInPutSources.equals(other$doWithInPutSources)) {
                  break label279;
               }

               return false;
            }

            Object this$httpInPutSources = this.getHttpInPutSources();
            Object other$httpInPutSources = other.getHttpInPutSources();
            if (this$httpInPutSources == null) {
               if (other$httpInPutSources != null) {
                  return false;
               }
            } else if (!this$httpInPutSources.equals(other$httpInPutSources)) {
               return false;
            }

            label265: {
               Object this$httpOutPutSources = this.getHttpOutPutSources();
               Object other$httpOutPutSources = other.getHttpOutPutSources();
               if (this$httpOutPutSources == null) {
                  if (other$httpOutPutSources == null) {
                     break label265;
                  }
               } else if (this$httpOutPutSources.equals(other$httpOutPutSources)) {
                  break label265;
               }

               return false;
            }

            label258: {
               Object this$fileInPutSources = this.getFileInPutSources();
               Object other$fileInPutSources = other.getFileInPutSources();
               if (this$fileInPutSources == null) {
                  if (other$fileInPutSources == null) {
                     break label258;
                  }
               } else if (this$fileInPutSources.equals(other$fileInPutSources)) {
                  break label258;
               }

               return false;
            }

            Object this$fileOutPutSources = this.getFileOutPutSources();
            Object other$fileOutPutSources = other.getFileOutPutSources();
            if (this$fileOutPutSources == null) {
               if (other$fileOutPutSources != null) {
                  return false;
               }
            } else if (!this$fileOutPutSources.equals(other$fileOutPutSources)) {
               return false;
            }

            Object this$webSocketServerSources = this.getWebSocketServerSources();
            Object other$webSocketServerSources = other.getWebSocketServerSources();
            if (this$webSocketServerSources == null) {
               if (other$webSocketServerSources != null) {
                  return false;
               }
            } else if (!this$webSocketServerSources.equals(other$webSocketServerSources)) {
               return false;
            }

            label237: {
               Object this$webSocketClientOutPutSources = this.getWebSocketClientOutPutSources();
               Object other$webSocketClientOutPutSources = other.getWebSocketClientOutPutSources();
               if (this$webSocketClientOutPutSources == null) {
                  if (other$webSocketClientOutPutSources == null) {
                     break label237;
                  }
               } else if (this$webSocketClientOutPutSources.equals(other$webSocketClientOutPutSources)) {
                  break label237;
               }

               return false;
            }

            label230: {
               Object this$webSocketClientInPutSources = this.getWebSocketClientInPutSources();
               Object other$webSocketClientInPutSources = other.getWebSocketClientInPutSources();
               if (this$webSocketClientInPutSources == null) {
                  if (other$webSocketClientInPutSources == null) {
                     break label230;
                  }
               } else if (this$webSocketClientInPutSources.equals(other$webSocketClientInPutSources)) {
                  break label230;
               }

               return false;
            }

            Object this$binLogPropertiesInPutSources = this.getBinLogPropertiesInPutSources();
            Object other$binLogPropertiesInPutSources = other.getBinLogPropertiesInPutSources();
            if (this$binLogPropertiesInPutSources == null) {
               if (other$binLogPropertiesInPutSources != null) {
                  return false;
               }
            } else if (!this$binLogPropertiesInPutSources.equals(other$binLogPropertiesInPutSources)) {
               return false;
            }

            label216: {
               Object this$redisDbs = this.getRedisDbs();
               Object other$redisDbs = other.getRedisDbs();
               if (this$redisDbs == null) {
                  if (other$redisDbs == null) {
                     break label216;
                  }
               } else if (this$redisDbs.equals(other$redisDbs)) {
                  break label216;
               }

               return false;
            }

            Object this$hbaseInPutSources = this.getHbaseInPutSources();
            Object other$hbaseInPutSources = other.getHbaseInPutSources();
            if (this$hbaseInPutSources == null) {
               if (other$hbaseInPutSources != null) {
                  return false;
               }
            } else if (!this$hbaseInPutSources.equals(other$hbaseInPutSources)) {
               return false;
            }

            label202: {
               Object this$hbaseOutPutSources = this.getHbaseOutPutSources();
               Object other$hbaseOutPutSources = other.getHbaseOutPutSources();
               if (this$hbaseOutPutSources == null) {
                  if (other$hbaseOutPutSources == null) {
                     break label202;
                  }
               } else if (this$hbaseOutPutSources.equals(other$hbaseOutPutSources)) {
                  break label202;
               }

               return false;
            }

            Object this$csvInPutSources = this.getCsvInPutSources();
            Object other$csvInPutSources = other.getCsvInPutSources();
            if (this$csvInPutSources == null) {
               if (other$csvInPutSources != null) {
                  return false;
               }
            } else if (!this$csvInPutSources.equals(other$csvInPutSources)) {
               return false;
            }

            Object this$csvOutPutSources = this.getCsvOutPutSources();
            Object other$csvOutPutSources = other.getCsvOutPutSources();
            if (this$csvOutPutSources == null) {
               if (other$csvOutPutSources != null) {
                  return false;
               }
            } else if (!this$csvOutPutSources.equals(other$csvOutPutSources)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SourceProperties;
   }

   public int hashCode() {
      int PRIME = 1;
      int result = 1;
      Object $publicConf = this.getPublicConf();
      result = result * 59 + ($publicConf == null ? 43 : $publicConf.hashCode());
      Object $dbs = this.getDbs();
      result = result * 59 + ($dbs == null ? 43 : $dbs.hashCode());
      Object $sqliteDbs = this.getSqliteDbs();
      result = result * 59 + ($sqliteDbs == null ? 43 : $sqliteDbs.hashCode());
      Object $mqttOutPutSources = this.getMqttOutPutSources();
      result = result * 59 + ($mqttOutPutSources == null ? 43 : $mqttOutPutSources.hashCode());
      Object $mqttInPutSources = this.getMqttInPutSources();
      result = result * 59 + ($mqttInPutSources == null ? 43 : $mqttInPutSources.hashCode());
      Object $mysqlOutSources = this.getMysqlOutSources();
      result = result * 59 + ($mysqlOutSources == null ? 43 : $mysqlOutSources.hashCode());
      Object $mysqlInSources = this.getMysqlInSources();
      result = result * 59 + ($mysqlInSources == null ? 43 : $mysqlInSources.hashCode());
      Object $links = this.getLinks();
      result = result * 59 + ($links == null ? 43 : $links.hashCode());
      Object $kafkaOutPutSources = this.getKafkaOutPutSources();
      result = result * 59 + ($kafkaOutPutSources == null ? 43 : $kafkaOutPutSources.hashCode());
      Object $kafkaInPutSources = this.getKafkaInPutSources();
      result = result * 59 + ($kafkaInPutSources == null ? 43 : $kafkaInPutSources.hashCode());
      Object $rabbitOutPutSources = this.getRabbitOutPutSources();
      result = result * 59 + ($rabbitOutPutSources == null ? 43 : $rabbitOutPutSources.hashCode());
      Object $rabbitInPutSources = this.getRabbitInPutSources();
      result = result * 59 + ($rabbitInPutSources == null ? 43 : $rabbitInPutSources.hashCode());
      Object $mongodbInPutSources = this.getMongodbInPutSources();
      result = result * 59 + ($mongodbInPutSources == null ? 43 : $mongodbInPutSources.hashCode());
      Object $mongodbOutPutSources = this.getMongodbOutPutSources();
      result = result * 59 + ($mongodbOutPutSources == null ? 43 : $mongodbOutPutSources.hashCode());
      Object $rocketmqInPutSources = this.getRocketmqInPutSources();
      result = result * 59 + ($rocketmqInPutSources == null ? 43 : $rocketmqInPutSources.hashCode());
      Object $rocketmqOutPutSources = this.getRocketmqOutPutSources();
      result = result * 59 + ($rocketmqOutPutSources == null ? 43 : $rocketmqOutPutSources.hashCode());
      Object $tcpClientOutPutSources = this.getTcpClientOutPutSources();
      result = result * 59 + ($tcpClientOutPutSources == null ? 43 : $tcpClientOutPutSources.hashCode());
      Object $tcpClientIntPutSources = this.getTcpClientIntPutSources();
      result = result * 59 + ($tcpClientIntPutSources == null ? 43 : $tcpClientIntPutSources.hashCode());
      Object $tcpServerOutPutSources = this.getTcpServerOutPutSources();
      result = result * 59 + ($tcpServerOutPutSources == null ? 43 : $tcpServerOutPutSources.hashCode());
      Object $tcpServerInPutSources = this.getTcpServerInPutSources();
      result = result * 59 + ($tcpServerInPutSources == null ? 43 : $tcpServerInPutSources.hashCode());
      Object $doWithInPutSources = this.getDoWithInPutSources();
      result = result * 59 + ($doWithInPutSources == null ? 43 : $doWithInPutSources.hashCode());
      Object $httpInPutSources = this.getHttpInPutSources();
      result = result * 59 + ($httpInPutSources == null ? 43 : $httpInPutSources.hashCode());
      Object $httpOutPutSources = this.getHttpOutPutSources();
      result = result * 59 + ($httpOutPutSources == null ? 43 : $httpOutPutSources.hashCode());
      Object $fileInPutSources = this.getFileInPutSources();
      result = result * 59 + ($fileInPutSources == null ? 43 : $fileInPutSources.hashCode());
      Object $fileOutPutSources = this.getFileOutPutSources();
      result = result * 59 + ($fileOutPutSources == null ? 43 : $fileOutPutSources.hashCode());
      Object $webSocketServerSources = this.getWebSocketServerSources();
      result = result * 59 + ($webSocketServerSources == null ? 43 : $webSocketServerSources.hashCode());
      Object $webSocketClientOutPutSources = this.getWebSocketClientOutPutSources();
      result = result * 59 + ($webSocketClientOutPutSources == null ? 43 : $webSocketClientOutPutSources.hashCode());
      Object $webSocketClientInPutSources = this.getWebSocketClientInPutSources();
      result = result * 59 + ($webSocketClientInPutSources == null ? 43 : $webSocketClientInPutSources.hashCode());
      Object $binLogPropertiesInPutSources = this.getBinLogPropertiesInPutSources();
      result = result * 59 + ($binLogPropertiesInPutSources == null ? 43 : $binLogPropertiesInPutSources.hashCode());
      Object $redisDbs = this.getRedisDbs();
      result = result * 59 + ($redisDbs == null ? 43 : $redisDbs.hashCode());
      Object $hbaseInPutSources = this.getHbaseInPutSources();
      result = result * 59 + ($hbaseInPutSources == null ? 43 : $hbaseInPutSources.hashCode());
      Object $hbaseOutPutSources = this.getHbaseOutPutSources();
      result = result * 59 + ($hbaseOutPutSources == null ? 43 : $hbaseOutPutSources.hashCode());
      Object $csvInPutSources = this.getCsvInPutSources();
      result = result * 59 + ($csvInPutSources == null ? 43 : $csvInPutSources.hashCode());
      Object $csvOutPutSources = this.getCsvOutPutSources();
      result = result * 59 + ($csvOutPutSources == null ? 43 : $csvOutPutSources.hashCode());
      return result;
   }

   public String toString() {
      return "SourceProperties(publicConf=" + this.getPublicConf() + ", dbs=" + this.getDbs() + ", sqliteDbs=" + this.getSqliteDbs() + ", mqttOutPutSources=" + this.getMqttOutPutSources() + ", mqttInPutSources=" + this.getMqttInPutSources() + ", mysqlOutSources=" + this.getMysqlOutSources() + ", mysqlInSources=" + this.getMysqlInSources() + ", links=" + this.getLinks() + ", kafkaOutPutSources=" + this.getKafkaOutPutSources() + ", kafkaInPutSources=" + this.getKafkaInPutSources() + ", rabbitOutPutSources=" + this.getRabbitOutPutSources() + ", rabbitInPutSources=" + this.getRabbitInPutSources() + ", mongodbInPutSources=" + this.getMongodbInPutSources() + ", mongodbOutPutSources=" + this.getMongodbOutPutSources() + ", rocketmqInPutSources=" + this.getRocketmqInPutSources() + ", rocketmqOutPutSources=" + this.getRocketmqOutPutSources() + ", tcpClientOutPutSources=" + this.getTcpClientOutPutSources() + ", tcpClientIntPutSources=" + this.getTcpClientIntPutSources() + ", tcpServerOutPutSources=" + this.getTcpServerOutPutSources() + ", tcpServerInPutSources=" + this.getTcpServerInPutSources() + ", doWithInPutSources=" + this.getDoWithInPutSources() + ", httpInPutSources=" + this.getHttpInPutSources() + ", httpOutPutSources=" + this.getHttpOutPutSources() + ", fileInPutSources=" + this.getFileInPutSources() + ", fileOutPutSources=" + this.getFileOutPutSources() + ", webSocketServerSources=" + this.getWebSocketServerSources() + ", webSocketClientOutPutSources=" + this.getWebSocketClientOutPutSources() + ", webSocketClientInPutSources=" + this.getWebSocketClientInPutSources() + ", redisDbs=" + this.getRedisDbs() + ", hbaseInPutSources=" + this.getHbaseInPutSources() + ", hbaseOutPutSources=" + this.getHbaseOutPutSources() + ", csvInPutSources=" + this.getCsvInPutSources() + ", csvOutPutSources=" + this.getCsvOutPutSources() + ")";
   }
}
