package com.chang.util.source.spring.start;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ObjectUtil;
import com.chang.util.source.mysqlBinLog.MySqlBinLogInput;
import com.chang.util.source.webSocketClient.WebSocketClientInPut;
import com.chang.util.source.webSocketClient.WebSocketClientOutPut;
import com.chang.util.source.webSocketServer.IWebSocketApi;
import com.chang.util.source.webSocketServer.WebSocketServer;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.BinLogDbProperties;
import com.chang.util.source.common.DataBaseApi;
import com.chang.util.source.common.HbaseFindType;
import com.chang.util.source.common.SqliteDataBaseApi;
import com.chang.util.source.csv.CsvInput;
import com.chang.util.source.csv.CsvOutput;
import com.chang.util.source.file.FileInput;
import com.chang.util.source.file.FileOutput;
//import com.chang.util.source.hbase.HbaseInput;
//import com.chang.util.source.hbase.HbaseOutput;
import com.chang.util.source.http.HttpInput;
import com.chang.util.source.http.HttpOutput;
import com.chang.util.source.kafka.KafkaInput;
import com.chang.util.source.kafka.KafkaOutput;
import com.chang.util.source.link.LinkSource;
import com.chang.util.source.log.LogOutput;
import com.chang.util.source.mongodb.MongodbInput;
import com.chang.util.source.mongodb.MongodbOutput;
import com.chang.util.source.mqtt.MqttInput;
import com.chang.util.source.mqtt.MqttOutput;
import com.chang.util.source.mqtt.Mqttv5Input;
import com.chang.util.source.mqtt.Mqttv5Output;
import com.chang.util.source.mysql.MySqlInput;
import com.chang.util.source.mysql.MySqlOutput;
import com.chang.util.source.print.PrintOutput;
import com.chang.util.source.rabbitmq.RabbitMqInput;
import com.chang.util.source.rabbitmq.RabbitMqOutput;
import com.chang.util.source.rocketmq.RocketmqInput;
import com.chang.util.source.rocketmq.RocketmqOutput;
import com.chang.util.source.tcpClient.TcpClientInPut;
import com.chang.util.source.tcpClient.TcpClientOutPut;
import com.chang.util.source.tcpServer.TcpServerInPut;
import com.chang.util.source.tcpServer.TcpServerOutPut;
import com.chang.util.source.manage.*;
import com.chang.util.source.spring.start.config.*;
import com.chang.uclass.bytecode.ClassUtils;
import com.chang.until.redisApi.lettuce.codec.MRedisTemplate;
import com.chang.until.redisApi.lettuce.conf.Client;
import com.chang.until.redisApi.lettuce.conf.ClusterClient;
import com.chang.until.redisApi.lettuce.conf.SentinelClient;
import com.chang.until.redisApi.lettuce.mode.ClusterMode;
import com.chang.until.redisApi.lettuce.mode.MasterSlaveMode;
import com.chang.until.redisApi.lettuce.mode.SentinelMode;
import com.chang.until.redisApi.lettuce.mode.SingleMode;
import com.chang.until.spring.context.SpringContext;
import com.chang.until.timeTaskApi.TimeTaskManage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

public class InitSource {
   private static final Logger log = LoggerFactory.getLogger(InitSource.class);
   private SourceProperties sourceProperties;
   public final Map<String, List<String>> rabbitConsumerKeys = new HashMap();

   public InitSource(SourceProperties sourceProperties) {
      this.sourceProperties = sourceProperties;
   }

   public void init() throws Exception {
      Map<String, DbProperties> dbs = this.sourceProperties.getDbs();
      if (MapUtil.isNotEmpty(dbs)) {
         Iterator var2 = dbs.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry<String, DbProperties> db = (Map.Entry)var2.next();
            DbProperties dbConfig = (DbProperties)db.getValue();
            DataBaseApi dataBaseApi = new DataBaseApi(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
            ManageMySqlSource.addDbSource((String)db.getKey(), dataBaseApi);
         }
      }

      Map<String, RedisProperties> redisDbs = this.sourceProperties.getRedisDbs();
      if (MapUtil.isNotEmpty(redisDbs)) {
         redisDbs.forEach((k, v) -> {
            RedisModeType redisMode = v.getRedisMode();
            Client client;
            if (ObjectUtil.equals(redisMode, RedisModeType.SINGLE)) {
               client = v.getClient();
               if (ObjectUtil.isNotEmpty(client)) {
                  SingleMode singleMode = new SingleMode(client);
                  ManageRedisSource.addRedisDb(k, singleMode);
               }
            } else if (ObjectUtil.equals(redisMode, RedisModeType.CLUSTER)) {
               ClusterClient clusterClient = v.getClusterClient();
               if (ObjectUtil.isNotEmpty(clusterClient)) {
                  ClusterMode clusterMode = new ClusterMode(clusterClient);
                  ManageRedisSource.addRedisDb(k, clusterMode);
               }
            } else if (ObjectUtil.equals(redisMode, RedisModeType.SENTINEL)) {
               SentinelClient sentinelClient = v.getSentinelClient();
               if (ObjectUtil.isNotEmpty(sentinelClient)) {
                  SentinelMode sentinelMode = new SentinelMode(sentinelClient);
                  ManageRedisSource.addRedisDb(k, sentinelMode);
               }
            } else {
               if (!ObjectUtil.equals(redisMode, RedisModeType.MASTER_SLAVE)) {
                  throw new RuntimeException("redisMode 配置错误");
               }

               client = v.getClient();
               if (ObjectUtil.isNotEmpty(client)) {
                  MasterSlaveMode masterSlaveMode = new MasterSlaveMode(client);
                  ManageRedisSource.addRedisDb(k, masterSlaveMode);
               }
            }

         });
      }

      Map<String, SqliteProperties> sqliteDbs = this.sourceProperties.getSqliteDbs();
      if (MapUtil.isNotEmpty(sqliteDbs)) {
         Iterator var47 = sqliteDbs.entrySet().iterator();

         while(var47.hasNext()) {
            Map.Entry<String, SqliteProperties> sqliteDb = (Map.Entry)var47.next();
            SqliteProperties sqliteConfig = (SqliteProperties)sqliteDb.getValue();
            String url = sqliteConfig.getUrl();
            String sqliteUrlPath = Utils.getSqliteUrlPath(url);
            if (!FileUtil.exist(sqliteUrlPath)) {
               FileUtil.mkdir(sqliteUrlPath);
            }

            SqliteDataBaseApi sqliteDataBaseApi = new SqliteDataBaseApi(url);
            ManageSqliteSource.addDbSource((String)sqliteDb.getKey(), sqliteDataBaseApi);
         }
      }

      Map<String, MqttInProperties> mqttInPutSources = this.sourceProperties.getMqttInPutSources();
      if (MapUtil.isNotEmpty(mqttInPutSources)) {
         Iterator var50 = mqttInPutSources.entrySet().iterator();

         while(var50.hasNext()) {
            Map.Entry<String, MqttInProperties> mqttIn = (Map.Entry)var50.next();
            MqttInProperties mqttConfig = (MqttInProperties)mqttIn.getValue();
            if (ObjectUtil.equals(mqttConfig.getVersion(), "v3")) {
               MqttInput mqttInput = new MqttInput(mqttConfig.getBroker(), mqttConfig.getTopic(), mqttConfig.getUsername(), mqttConfig.getPassword(), (String)mqttIn.getKey(), mqttConfig.getSslEnable(), mqttConfig.getCaCrtFile(), mqttConfig.getCrtFile(), mqttConfig.getKeyFile(), mqttConfig.getSslPassword(), mqttConfig.getMessageType(), mqttConfig.getParm(), mqttConfig.isRetained(), mqttConfig.getSubscribersQos(), mqttConfig.isAutoGenerationClientId(), mqttConfig.getClientID(), mqttConfig.getNThreads(), mqttConfig.getMaximumQueueSize());
               ManageSource.addInputSource((String)mqttIn.getKey(), mqttInput);
            } else {
               if (!ObjectUtil.equals(mqttConfig.getVersion(), "v5")) {
                  throw new RuntimeException("Mqtt Version err");
               }

               Mqttv5Input mqttv5Input = new Mqttv5Input(mqttConfig.getBroker(), mqttConfig.getTopic(), mqttConfig.getUsername(), mqttConfig.getPassword(), (String)mqttIn.getKey(), mqttConfig.getSslEnable(), mqttConfig.getCaCrtFile(), mqttConfig.getCrtFile(), mqttConfig.getKeyFile(), mqttConfig.getSslPassword(), mqttConfig.getMessageType(), mqttConfig.getParm(), mqttConfig.isRetained(), mqttConfig.getSubscribersQos(), mqttConfig.isAutoGenerationClientId(), mqttConfig.getClientID(), mqttConfig.getNThreads(), mqttConfig.getMaximumQueueSize());
               ManageSource.addInputSource((String)mqttIn.getKey(), mqttv5Input);
            }
         }
      }

      Map<String, MqttOutProperties> mqttOutPutSources = this.sourceProperties.getMqttOutPutSources();
      PublicProperties publicProperties = this.sourceProperties.getPublicConf();
      if (ObjectUtil.isNotNull(publicProperties) && ObjectUtil.isNotNull(publicProperties.getEnableMqttOutPrint())) {
         MqttOutput.setIsPrint(publicProperties.getEnableMqttOutPrint());
      }

      if (MapUtil.isNotEmpty(mqttOutPutSources)) {
         Iterator var55 = mqttOutPutSources.entrySet().iterator();

         while(var55.hasNext()) {
            Map.Entry<String, MqttOutProperties> mqttOut = (Map.Entry)var55.next();
            MqttOutProperties mqttConfig = (MqttOutProperties)mqttOut.getValue();
            if (mqttConfig.getIsCache() && StringUtils.isBlank(mqttConfig.getCacheTopic())) {
               throw new RuntimeException("cacheTopic must not Blank");
            }

            if (ObjectUtil.equals(mqttConfig.getVersion(), "v3")) {
               MqttOutput mqttOutput = new MqttOutput(mqttConfig.getBroker(), mqttConfig.getTopic(), mqttConfig.getUsername(), mqttConfig.getPassword(), mqttConfig.getIsCache(), mqttConfig.getCacheName(), mqttConfig.getCacheTopic(), mqttConfig.getBatchSize(), mqttConfig.getTimeInterval(), mqttConfig.getExpiredDataSize(), (String)mqttOut.getKey(), mqttConfig.getQueuePopSize(), mqttConfig.getSslEnable(), mqttConfig.getCaCrtFile(), mqttConfig.getCrtFile(), mqttConfig.getKeyFile(), mqttConfig.getSslPassword(), mqttConfig.getMessageType(), mqttConfig.getParm(), mqttConfig.isRetained(), mqttConfig.isAutoGenerationClientId(), mqttConfig.getClientID(), mqttConfig.getWillPayLoad(), mqttConfig.getPublishQos());
               ManageSource.addOutputSource((String)mqttOut.getKey(), mqttOutput);
            } else {
               if (!ObjectUtil.equals(mqttConfig.getVersion(), "v5")) {
                  throw new RuntimeException("Mqtt Version err");
               }

               Mqttv5Output mqttv5Output = new Mqttv5Output(mqttConfig.getBroker(), mqttConfig.getTopic(), mqttConfig.getUsername(), mqttConfig.getPassword(), mqttConfig.getIsCache(), mqttConfig.getCacheName(), mqttConfig.getCacheTopic(), mqttConfig.getBatchSize(), mqttConfig.getTimeInterval(), mqttConfig.getExpiredDataSize(), (String)mqttOut.getKey(), mqttConfig.getQueuePopSize(), mqttConfig.getSslEnable(), mqttConfig.getCaCrtFile(), mqttConfig.getCrtFile(), mqttConfig.getKeyFile(), mqttConfig.getSslPassword(), mqttConfig.getMessageType(), mqttConfig.getParm(), mqttConfig.isRetained(), mqttConfig.isAutoGenerationClientId(), mqttConfig.getClientID(), mqttConfig.getWillPayLoad(), mqttConfig.getPublishQos());
               ManageSource.addOutputSource((String)mqttOut.getKey(), mqttv5Output);
            }
         }
      }

      Map<String, MySqlOutSourceProperties> mysqlOutSources = this.sourceProperties.getMysqlOutSources();
      String[] fields;
      int var16;
      if (MapUtil.isNotEmpty(mysqlOutSources)) {
         Iterator var60 = mysqlOutSources.entrySet().iterator();

         while(var60.hasNext()) {
            Map.Entry<String, MySqlOutSourceProperties> mysqlOut = (Map.Entry)var60.next();
            MySqlOutSourceProperties outConfig = (MySqlOutSourceProperties)mysqlOut.getValue();
            MySqlOutput mySqlOutput = new MySqlOutput(outConfig.getDbConfigName(), outConfig.getTableName(), (String)mysqlOut.getKey(), outConfig.getParm());
            String excludeFiled = outConfig.getExcludeFiled();
            fields = excludeFiled.split(",");
            fields = fields;
            int var15 = fields.length;

            for(var16 = 0; var16 < var15; ++var16) {
               String filed = fields[var16];
               mySqlOutput.addExcludeFiled(filed);
            }

            ManageSource.addOutputSource((String)mysqlOut.getKey(), mySqlOutput);
         }
      }

      Map<String, MySqlInSourceProperties> mysqlInSources = this.sourceProperties.getMysqlInSources();
      if (MapUtil.isNotEmpty(mysqlInSources)) {
         Iterator var64 = mysqlInSources.entrySet().iterator();

         while(var64.hasNext()) {
            Map.Entry<String, MySqlInSourceProperties> mysqlIn = (Map.Entry)var64.next();
            MySqlInSourceProperties inConfig = (MySqlInSourceProperties)mysqlIn.getValue();
            MySqlInput mySqlInput = new MySqlInput(inConfig.getSql(), inConfig.getDbConfigName(), (String)mysqlIn.getKey(), inConfig.getTime(), inConfig.getTaskModeType());
            String excludeFiled = inConfig.getExcludeFiled();
            fields = excludeFiled.split(",");
            String[] var88 = fields;
            var16 = fields.length;

            for(int var98 = 0; var98 < var16; ++var98) {
               String filed = var88[var98];
               mySqlInput.addExcludeFiled(filed);
            }

            ManageSource.addInputSource((String)mysqlIn.getKey(), mySqlInput);
         }
      }

      Map<String, KafkaProperties> kafkaInPutSources = this.sourceProperties.getKafkaInPutSources();
      if (MapUtil.isNotEmpty(kafkaInPutSources)) {
         Iterator var69 = kafkaInPutSources.entrySet().iterator();

         while(var69.hasNext()) {
            Map.Entry<String, KafkaProperties> kafkaIn = (Map.Entry)var69.next();
            KafkaProperties kafkaInConfig = (KafkaProperties)kafkaIn.getValue();
            KafkaInput kafkaInput = new KafkaInput(kafkaInConfig.getServers(), kafkaInConfig.getGroupId(), kafkaInConfig.getTopic(), (String)kafkaIn.getKey(), kafkaInConfig.getMessageType(), kafkaInConfig.getParm());
            ManageSource.addInputSource((String)kafkaIn.getKey(), kafkaInput);
         }
      }

      Map<String, KafkaProperties> kafkaOutPutSources = this.sourceProperties.getKafkaOutPutSources();
      if (MapUtil.isNotEmpty(kafkaOutPutSources)) {
         Iterator var73 = kafkaOutPutSources.entrySet().iterator();

         while(var73.hasNext()) {
            Map.Entry<String, KafkaProperties> kafkaOut = (Map.Entry)var73.next();
            KafkaProperties kafkaOutConfig = (KafkaProperties)kafkaOut.getValue();
            KafkaOutput kafkaOutput = new KafkaOutput(kafkaOutConfig.getServers(), kafkaOutConfig.getGroupId(), kafkaOutConfig.getTopic(), (String)kafkaOut.getKey(), kafkaOutConfig.getMessageType(), kafkaOutConfig.getParm());
            ManageSource.addOutputSource((String)kafkaOut.getKey(), kafkaOutput);
         }
      }

      Map<String, RabbitMqProperties> rabbitMqInPutSources = this.sourceProperties.getRabbitInPutSources();
      if (MapUtil.isNotEmpty(rabbitMqInPutSources)) {
         Iterator var79 = rabbitMqInPutSources.entrySet().iterator();

         while(var79.hasNext()) {
            Map.Entry<String, RabbitMqProperties> rabbitInt = (Map.Entry)var79.next();
            RabbitMqProperties rabbitConfig = (RabbitMqProperties)rabbitInt.getValue();
            List<String> keys = new ArrayList();

            for(Integer i = 1; i <= rabbitConfig.getConsumerNum(); i = i + 1) {
               RabbitMqInput rabbitMqInput = new RabbitMqInput(rabbitConfig.getHost(), rabbitConfig.getPort(), rabbitConfig.getUsername(), rabbitConfig.getPassword(), rabbitConfig.getQueue(), (String)rabbitInt.getKey(), rabbitConfig.getMessageType(), rabbitConfig.getBasicQos(), rabbitConfig.getIsAck(), rabbitConfig.getNThreads(), rabbitConfig.getMaximumQueueSize(), rabbitConfig.getParm(), rabbitConfig.getTopic(), rabbitConfig.getIsDurable());
               ManageSource.addInputSource((String)rabbitInt.getKey() + "_" + i, rabbitMqInput);
               keys.add((String)rabbitInt.getKey() + "_" + i);
            }

            this.rabbitConsumerKeys.put(rabbitInt.getKey(), keys);
         }
      }

      Map<String, RabbitMqProperties> rabbitMqOutPutSources = this.sourceProperties.getRabbitOutPutSources();
      if (MapUtil.isNotEmpty(rabbitMqOutPutSources)) {
         Iterator var85 = rabbitMqOutPutSources.entrySet().iterator();

         while(var85.hasNext()) {
            Map.Entry<String, RabbitMqProperties> rabbitOut = (Map.Entry)var85.next();
            RabbitMqProperties rabbitConfig = (RabbitMqProperties)rabbitOut.getValue();
            RabbitMqOutput rabbitMqOutput = new RabbitMqOutput(rabbitConfig.getHost(), rabbitConfig.getPort(), rabbitConfig.getUsername(), rabbitConfig.getPassword(), rabbitConfig.getTopic(), (String)rabbitOut.getKey(), rabbitConfig.getMessageType(), rabbitConfig.getIsDurable(), rabbitConfig.getParm());
            ManageSource.addOutputSource((String)rabbitOut.getKey(), rabbitMqOutput);
         }
      }

      Map<String, MongodbInProperties> mongodbInPutSources = this.sourceProperties.getMongodbInPutSources();
      if (MapUtil.isNotEmpty(mongodbInPutSources)) {
         Iterator var91 = mongodbInPutSources.entrySet().iterator();

         while(var91.hasNext()) {
            Map.Entry<String, MongodbInProperties> mongodbIn = (Map.Entry)var91.next();
            MongodbInProperties mongodbConfig = (MongodbInProperties)mongodbIn.getValue();
            MongodbInput mongodbInput = new MongodbInput(mongodbConfig.getUrl(), mongodbConfig.getDatabaseName(), mongodbConfig.getCollectionName(), mongodbConfig.getConditions(), (String)mongodbIn.getKey(), mongodbConfig.getTime(), mongodbConfig.getTaskModeType(), mongodbConfig.getParm());
            ManageSource.addInputSource((String)mongodbIn.getKey(), mongodbInput);
         }
      }

      Map<String, MongodbOutProperties> mongodbOutPutSources = this.sourceProperties.getMongodbOutPutSources();
      if (MapUtil.isNotEmpty(mongodbOutPutSources)) {
         Iterator var95 = mongodbOutPutSources.entrySet().iterator();

         while(var95.hasNext()) {
            Map.Entry<String, MongodbOutProperties> mongodbOut = (Map.Entry)var95.next();
            MongodbOutProperties mongodbConfig = (MongodbOutProperties)mongodbOut.getValue();
            MongodbOutput mongodbOutput = new MongodbOutput(mongodbConfig.getUrl(), mongodbConfig.getDatabaseName(), mongodbConfig.getCollectionName(), (String)mongodbOut.getKey(), mongodbConfig.getParm());
            ManageSource.addOutputSource((String)mongodbOut.getKey(), mongodbOutput);
         }
      }

      Map<String, RocketmqInProperties> rocketmqInSources = this.sourceProperties.getRocketmqInPutSources();
      if (MapUtil.isNotEmpty(rocketmqInSources)) {
         Iterator var105 = rocketmqInSources.entrySet().iterator();

         label375:
         while(true) {
            RocketmqInProperties rocketmqConfig;
            Map.Entry rocketmqIn;
            while(true) {
               if (!var105.hasNext()) {
                  break label375;
               }

               rocketmqIn = (Map.Entry)var105.next();
               rocketmqConfig = (RocketmqInProperties)rocketmqIn.getValue();
               if (!rocketmqConfig.getIsSingle()) {
                  break;
               }

               if (StringUtils.isBlank(rocketmqConfig.getRedisKey())) {
                  throw new RuntimeException("RedisKey is empty!");
               }

               final MRedisTemplate redisDb = ManageRedisSource.getRedisDb(rocketmqConfig.getRedisKey());
               Boolean hasRocketmq = (Boolean)redisDb.get(rocketmqConfig.getTopic(), Boolean.class);
               if (ObjectUtil.isNull(hasRocketmq)) {
                  redisDb.set(rocketmqConfig.getTopic(), true, 2L, TimeUnit.SECONDS);
                  RocketmqInProperties finalRocketmqConfig = rocketmqConfig;
                  TimeTaskManage.scheduleAtFixedRate(rocketmqConfig.getTopic(), new Runnable() {
                     public void run() {
                        try {
                           redisDb.set(finalRocketmqConfig.getTopic(), true, 2L, TimeUnit.SECONDS);
                        } catch (Exception var2) {
                           InitSource.log.error("redis err: ", var2);
                           throw new RuntimeException(var2);
                        }
                     }
                  }, 1L, TimeUnit.SECONDS);
                  break;
               }
            }

            RocketmqInput rocketmqInput = new RocketmqInput((String)rocketmqIn.getKey(), rocketmqConfig.getConsumerGroup(), rocketmqConfig.getNameSrvAddr(), rocketmqConfig.getTopic(), rocketmqConfig.getSubExpression(), rocketmqConfig.getMessageModel(), rocketmqConfig.getNamespace(), rocketmqConfig.getMessageType(), rocketmqConfig.getMessageBatchMaxSize(), rocketmqConfig.getConsumeThread(), rocketmqConfig.getAccessKey(), rocketmqConfig.getSecretKey(), rocketmqConfig.getConsumerMessageType(), rocketmqConfig.getParm());
            ManageSource.addInputSource((String)rocketmqIn.getKey(), rocketmqInput);
         }
      }

      Map<String, RocketmqOutProperties> rocketmqOutSources = this.sourceProperties.getRocketmqOutPutSources();
      if (MapUtil.isNotEmpty(rocketmqOutSources)) {
         Iterator var111 = rocketmqOutSources.entrySet().iterator();

         while(var111.hasNext()) {
            Map.Entry<String, RocketmqOutProperties> rocketmqOut = (Map.Entry)var111.next();
            RocketmqOutProperties rocketmqConfig = (RocketmqOutProperties)rocketmqOut.getValue();
            RocketmqOutput rocketmqOutput = new RocketmqOutput((String)rocketmqOut.getKey(), rocketmqConfig.getProducerGroup(), rocketmqConfig.getNameSrvAddr(), rocketmqConfig.getTopic(), rocketmqConfig.getSendMsgTimeout(), rocketmqConfig.getNamespace(), rocketmqConfig.getAccessKey(), rocketmqConfig.getSecretKey(), rocketmqConfig.getMessageType(), rocketmqConfig.getParm());
            ManageSource.addOutputSource((String)rocketmqOut.getKey(), rocketmqOutput);
         }
      }

      Map<String, TcpClientProperties> tcpClientIntPutSources = this.sourceProperties.getTcpClientIntPutSources();
      if (MapUtil.isNotEmpty(tcpClientIntPutSources)) {
         Iterator var112 = tcpClientIntPutSources.entrySet().iterator();

         while(var112.hasNext()) {
            Map.Entry<String, TcpClientProperties> tcpClientInPut = (Map.Entry)var112.next();
            TcpClientProperties config = (TcpClientProperties)tcpClientInPut.getValue();
            TcpClientInPut tcpInPut = new TcpClientInPut((String)tcpClientInPut.getKey(), config.getHostname(), config.getPort(), config.getGroupName(), config.getMessageType(), config.getParm(), config.getNThreads(), config.getMaximumQueueSize());
            ManageSource.addInputSource((String)tcpClientInPut.getKey(), tcpInPut);
         }
      }

      Map<String, TcpClientProperties> tcpClientOutPutSources = this.sourceProperties.getTcpClientOutPutSources();
      if (MapUtil.isNotEmpty(tcpClientOutPutSources)) {
         Iterator var119 = tcpClientOutPutSources.entrySet().iterator();

         while(var119.hasNext()) {
            Map.Entry<String, TcpClientProperties> tcpClientOutPut = (Map.Entry)var119.next();
            TcpClientProperties config = (TcpClientProperties)tcpClientOutPut.getValue();
            TcpClientOutPut clientOutPut = new TcpClientOutPut((String)tcpClientOutPut.getKey(), config.getHostname(), config.getPort(), config.getGroupName(), config.getMessageType(), config.getParm());
            ManageSource.addOutputSource((String)tcpClientOutPut.getKey(), clientOutPut);
         }
      }

      Map<String, TcpServerProperties> tcpServerInPutSources = this.sourceProperties.getTcpServerInPutSources();
      if (MapUtil.isNotEmpty(tcpServerInPutSources)) {
         Iterator var123 = tcpServerInPutSources.entrySet().iterator();

         while(var123.hasNext()) {
            Map.Entry<String, TcpServerProperties> tcpServerInPut = (Map.Entry)var123.next();
            TcpServerProperties config = (TcpServerProperties)tcpServerInPut.getValue();
            TcpServerInPut serverInPut = new TcpServerInPut((String)tcpServerInPut.getKey(), config.getGroupNamesMode(), config.getHostname(), config.getPort(), config.getMessageType(), config.getParm());
            ManageSource.addInputSource((String)tcpServerInPut.getKey(), serverInPut);
         }
      }

      Map<String, TcpServerProperties> tcpServerOutPutSources = this.sourceProperties.getTcpServerOutPutSources();
      if (MapUtil.isNotEmpty(tcpServerOutPutSources)) {
         Iterator var127 = tcpServerOutPutSources.entrySet().iterator();

         while(var127.hasNext()) {
            Map.Entry<String, TcpServerProperties> tcpServerOutPut = (Map.Entry)var127.next();
            TcpServerProperties config = (TcpServerProperties)tcpServerOutPut.getValue();
            TcpServerOutPut serverOutPut = new TcpServerOutPut((String)tcpServerOutPut.getKey(), config.getGroupNamesMode(), config.getHostname(), config.getPort(), config.getMessageType(), config.getParm());
            ManageSource.addOutputSource((String)tcpServerOutPut.getKey(), serverOutPut);
         }
      }

      Map<String, HttpInProperties> httpInPutSources = this.sourceProperties.getHttpInPutSources();
      if (MapUtil.isNotEmpty(httpInPutSources)) {
         Iterator var131 = httpInPutSources.entrySet().iterator();

         while(var131.hasNext()) {
            Map.Entry<String, HttpInProperties> httpInEntry = (Map.Entry)var131.next();
            HttpInProperties config = (HttpInProperties)httpInEntry.getValue();
            HttpInput httpInput = new HttpInput(config.getBaseUrl(), config.getUrl(), config.getType(), (String)httpInEntry.getKey(), config.getTime(), (Map)(config.getParm() == null ? new HashMap() : config.getParm()), config.getExParm());
            ManageSource.addInputSource((String)httpInEntry.getKey(), httpInput);
         }
      }

      Map<String, HttpOutProperties> httpOutPutSources = this.sourceProperties.getHttpOutPutSources();
      if (MapUtil.isNotEmpty(httpOutPutSources)) {
         Iterator var135 = httpOutPutSources.entrySet().iterator();

         while(var135.hasNext()) {
            Map.Entry<String, HttpOutProperties> httpOutEntry = (Map.Entry)var135.next();
            HttpOutProperties config = (HttpOutProperties)httpOutEntry.getValue();
            HttpOutput httpOutput = new HttpOutput((String)httpOutEntry.getKey(), config.getBaseUrl(), config.getUrl(), config.getType(), config.getParm());
            ManageSource.addOutputSource((String)httpOutEntry.getKey(), httpOutput);
         }
      }

      Map<String, FileInProperties> fileInPutSources = this.sourceProperties.getFileInPutSources();
      if (MapUtil.isNotEmpty(fileInPutSources)) {
         Iterator var139 = fileInPutSources.entrySet().iterator();

         while(var139.hasNext()) {
            Map.Entry<String, FileInProperties> fileInEntry = (Map.Entry)var139.next();
            FileInProperties config = (FileInProperties)fileInEntry.getValue();
            FileInput fileInput = new FileInput((String)fileInEntry.getKey(), config.getPath(), config.getTime(), config.getTaskModeType(), config.getParm());
            ManageSource.addInputSource((String)fileInEntry.getKey(), fileInput);
         }
      }

      Map<String, FileOutProperties> fileOutPutSources = this.sourceProperties.getFileOutPutSources();
      if (MapUtil.isNotEmpty(fileOutPutSources)) {
         Iterator var143 = fileOutPutSources.entrySet().iterator();

         while(var143.hasNext()) {
            Map.Entry<String, FileOutProperties> fileOutEntry = (Map.Entry)var143.next();
            FileOutProperties config = (FileOutProperties)fileOutEntry.getValue();
            FileOutput fileOutput = new FileOutput((String)fileOutEntry.getKey(), config.getPath(), config.getParm());
            ManageSource.addOutputSource((String)fileOutEntry.getKey(), fileOutput);
         }
      }

      Map<String, CsvInProperties> csvInPutSources = this.sourceProperties.getCsvInPutSources();
      if (MapUtil.isNotEmpty(csvInPutSources)) {
         Iterator var147 = csvInPutSources.entrySet().iterator();

         while(var147.hasNext()) {
            Map.Entry<String, CsvInProperties> csvEntry = (Map.Entry)var147.next();
            CsvInProperties config = (CsvInProperties)csvEntry.getValue();
            CsvInput csvInput = new CsvInput((String)csvEntry.getKey(), config.getFileFullPath(), ClassLoaderUtil.getClassLoader().loadClass(config.getClazzName()), config.getTime(), config.getTaskModeType(), config.getParm());
            ManageSource.addInputSource((String)csvEntry.getKey(), csvInput);
         }
      }

      Map<String, CsvOutProperties> csvOutPutSources = this.sourceProperties.getCsvOutPutSources();
      if (MapUtil.isNotEmpty(csvOutPutSources)) {
         Iterator var151 = csvOutPutSources.entrySet().iterator();

         while(var151.hasNext()) {
            Map.Entry<String, CsvOutProperties> csvEntry = (Map.Entry)var151.next();
            CsvOutProperties config = (CsvOutProperties)csvEntry.getValue();
            CsvOutput csvOutput = new CsvOutput((String)csvEntry.getKey(), config.getFilePath(), config.getFileName(), config.getFileMaxSize(), config.getParm());
            ManageSource.addOutputSource((String)csvEntry.getKey(), csvOutput);
         }
      }

      Map<String, WebSocketServerProperties> webSocketServerSources = this.sourceProperties.getWebSocketServerSources();
      if (MapUtil.isNotEmpty(webSocketServerSources)) {
         Iterator var155 = webSocketServerSources.entrySet().iterator();

         while(var155.hasNext()) {
            Map.Entry<String, WebSocketServerProperties> wssInPutEntry = (Map.Entry)var155.next();
            WebSocketServerProperties config = (WebSocketServerProperties)wssInPutEntry.getValue();
            String socketApiBeanName = config.getSocketApiBeanName();
            WebSocketServer webSocketServer = null;
            if (StringUtils.isNoneBlank(new CharSequence[]{socketApiBeanName})) {
               Object socketApi = SpringContext.getBean(socketApiBeanName);
               Set<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(socketApi.getClass(), new Predicate[]{(clazz) -> {
                  return clazz.equals(IWebSocketApi.class);
               }});
               if (allInterfaces.size() == 0) {
                  throw new RuntimeException(socketApi.getClass().getName() + " is not implements " + IWebSocketApi.class.getName());
               }

               webSocketServer = new WebSocketServer(config.getPort(), (String)wssInPutEntry.getKey(), config.getMessageType(), (IWebSocketApi)IWebSocketApi.class.cast(socketApi), config.getParm());
            } else {
               webSocketServer = new WebSocketServer(config.getPort(), (String)wssInPutEntry.getKey(), config.getMessageType(), (IWebSocketApi)null, config.getParm());
            }

            ManageSource.addInputSource((String)wssInPutEntry.getKey(), webSocketServer);
            ManageSource.addOutputSource((String)wssInPutEntry.getKey(), webSocketServer);
         }
      }

      Map<String, WebSocketClientProperties> webSocketClientInPutSources = this.sourceProperties.getWebSocketClientInPutSources();
      if (MapUtil.isNotEmpty(webSocketClientInPutSources)) {
         Iterator var159 = webSocketClientInPutSources.entrySet().iterator();

         while(var159.hasNext()) {
            Map.Entry<String, WebSocketClientProperties> wscInPutEntry = (Map.Entry)var159.next();
            WebSocketClientProperties config = (WebSocketClientProperties)wscInPutEntry.getValue();
            WebSocketClientInPut webSocketClientInPut = new WebSocketClientInPut(config.getUrl(), (String)wscInPutEntry.getKey(), config.getParm(), config.getMessageType());
            ManageSource.addInputSource((String)wscInPutEntry.getKey(), webSocketClientInPut);
         }
      }

      Map<String, WebSocketClientProperties> webSocketClientOutPutSources = this.sourceProperties.getWebSocketClientOutPutSources();
      if (MapUtil.isNotEmpty(webSocketClientOutPutSources)) {
         Iterator var163 = webSocketClientOutPutSources.entrySet().iterator();

         while(var163.hasNext()) {
            Map.Entry<String, WebSocketClientProperties> wscOutPutEntry = (Map.Entry)var163.next();
            WebSocketClientProperties config = (WebSocketClientProperties)wscOutPutEntry.getValue();
            WebSocketClientOutPut webSocketClientOutPut = new WebSocketClientOutPut(config.getUrl(), (String)wscOutPutEntry.getKey(), config.getParm(), config.getMessageType());
            ManageSource.addOutputSource((String)wscOutPutEntry.getKey(), webSocketClientOutPut);
         }
      }

      BinLogProperties binLogPropertiesInPutSources = this.sourceProperties.getBinLogPropertiesInPutSources();
      Map<String, BinLogDbWatchProperties> hbaseInPutSources;
      if (ObjectUtil.isNotNull(binLogPropertiesInPutSources)) {
         hbaseInPutSources = binLogPropertiesInPutSources.getBinLogDbPropertiesMap();
         if (MapUtil.isNotEmpty(hbaseInPutSources)) {
            Map<String, BinLogDbProperties> binLogDbPropertiesMap = new HashMap();
            hbaseInPutSources.forEach((keyx, configx) -> {
               BinLogDbProperties binLogDbProperties = new BinLogDbProperties();
               binLogDbProperties.setHost(configx.getHost());
               binLogDbProperties.setPort(configx.getPort());
               binLogDbProperties.setUserName(configx.getUserName());
               binLogDbProperties.setPassword(configx.getPassword());
               binLogDbProperties.setDatabase(configx.getDatabase());
               binLogDbProperties.setSyncDbType(configx.getSyncDbType());
               binLogDbProperties.setLookTableList(configx.getLookTableList());
               binLogDbProperties.setBlacklist(configx.getBlacklist());
               binLogDbPropertiesMap.put(keyx, binLogDbProperties);
            });
            String binLogRedisKey = this.sourceProperties.getBinLogPropertiesInPutSources().getBinLogRedisKey();
            if (StringUtils.isBlank(binLogRedisKey)) {
               throw new RuntimeException("binLogRedisKey is null");
            }

            MRedisTemplate redisDb = ManageRedisSource.getRedisDb(binLogRedisKey);
            if (ObjectUtil.isNull(redisDb)) {
               log.info("redisKey:{} 未找到redis连接", binLogRedisKey);
               throw new RuntimeException("redisDb is null");
            }

            MySqlBinLogInput binLogInput = new MySqlBinLogInput(binLogDbPropertiesMap, redisDb, this.sourceProperties.getBinLogPropertiesInPutSources().getParm());
            ManageSource.addInputSource(this.sourceProperties.getBinLogPropertiesInPutSources().getSourceKey(), binLogInput);
         }
      }

      /*Map<String, HbaseInProperties> hbaseInPutSources2 = this.sourceProperties.getHbaseInPutSources();
      if (MapUtil.isNotEmpty(hbaseInPutSources2)) {
         hbaseInPutSources2.forEach((keyx, configx) -> {
            HbaseInput hbaseInput;
            if (ObjectUtil.equals(configx.getHbaseFindType(), HbaseFindType.ROW_KEY_PREFIX)) {
               try {
                  if (StringUtils.isBlank(configx.getRowKeyPrefix())) {
                     throw new RuntimeException("rowKeyPrefix is null");
                  }

                  hbaseInput = new HbaseInput(configx.getMaster(), configx.getZookeeper(), configx.getTableName(), configx.getFamilyName(), ClassLoaderUtil.getClassLoader().loadClass(configx.getClazzName()), configx.getRowKeyPrefix(), keyx, configx.getTime(), configx.getTaskModeType(), configx.getIsAuth(), configx.getUser(), configx.getKrbConfPath(), configx.getKeytabPath(), configx.getParm());
                  ManageSource.addInputSource(keyx, hbaseInput);
               } catch (Exception var3) {
                  throw new RuntimeException(var3);
               }
            } else if (ObjectUtil.equals(configx.getHbaseFindType(), HbaseFindType.START_AND_END)) {
               try {
                  if (StringUtils.isBlank(configx.getStartRow()) || StringUtils.isBlank(configx.getEndRow())) {
                     throw new RuntimeException("startRow and endRow is null");
                  }

                  hbaseInput = new HbaseInput(configx.getMaster(), configx.getZookeeper(), configx.getTableName(), configx.getFamilyName(), ClassLoaderUtil.getClassLoader().loadClass(configx.getClazzName()), configx.getStartRow(), configx.getEndRow(), keyx, configx.getTime(), configx.getTaskModeType(), configx.getIsAuth(), configx.getUser(), configx.getKrbConfPath(), configx.getKeytabPath(), configx.getParm());
                  ManageSource.addInputSource(keyx, hbaseInput);
               } catch (Exception var4) {
                  throw new RuntimeException(var4);
               }
            }

         });
      }

      Map<String, HbaseOutProperties> hbaseOutPutSources = this.sourceProperties.getHbaseOutPutSources();
      if (MapUtil.isNotEmpty(hbaseOutPutSources)) {
         hbaseOutPutSources.forEach((keyx, configx) -> {
            HbaseOutput hbaseOutput = new HbaseOutput(configx.getMaster(), configx.getZookeeper(), configx.getTableName(), configx.getFamilyName(), configx.getIsAuth(), configx.getUser(), configx.getKrbConfPath(), configx.getKeytabPath(), keyx, configx.getParm());
            ManageSource.addOutputSource(keyx, hbaseOutput);
         });
      }*/

      ManageSource.addOutputSource("LogOutput", new LogOutput());
      ManageSource.addOutputSource("PrintOutput", new PrintOutput());
      Map<String, LinkProperties> linkProperties = this.sourceProperties.getLinks();
      if (ObjectUtil.isNotNull(linkProperties)) {
         Iterator var176 = linkProperties.entrySet().iterator();

         label258:
         while(true) {
            while(true) {
               if (!var176.hasNext()) {
                  break label258;
               }

               Map.Entry<String, LinkProperties> link = (Map.Entry)var176.next();
               LinkProperties linkConfig = (LinkProperties)link.getValue();
               InputSource inputSource = ManageSource.getInputSource(linkConfig.getFrom());
               if (ObjectUtil.isNull(inputSource)) {
                  log.error("config inputSource name map value null");
               } else {
                  OutputSource outputSource = ManageSource.getOutputSource(linkConfig.getTo());
                  if (ObjectUtil.isNull(outputSource)) {
                     log.error("config outputSource name map value null");
                  } else {
                     LinkSource linkSource;
                     if (StringUtils.isNotEmpty(linkConfig.getFunctionBeanClass())) {
                        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                        Class<?> aClass = contextClassLoader.loadClass(linkConfig.getFunctionBeanClass());
                        Object bean = aClass.newInstance();
                        if (!(bean instanceof Function)) {
                           log.error("Function type ERR!");
                           continue;
                        }

                        linkSource = new LinkSource(inputSource, outputSource, (Function)Function.class.cast(bean));
                     } else {
                        linkSource = new LinkSource(inputSource, outputSource, (Function)null);
                     }

                     ManageLink.addLinkSource((String)link.getKey(), linkSource);
                     linkSource.runLink();
                  }
               }
            }
         }
      }

      Map<String, InputSourceDoWithProperties> doWithInPutSources = this.sourceProperties.getDoWithInPutSources();
      if (ObjectUtil.isNotNull(doWithInPutSources)) {
         Iterator var179 = doWithInPutSources.entrySet().iterator();

         while(true) {
            while(true) {
               Map.Entry doWith;
               InputSourceDoWithProperties doWithValue;
               String beanName;
               do {
                  if (!var179.hasNext()) {
                     return;
                  }

                  doWith = (Map.Entry)var179.next();
                  doWithValue = (InputSourceDoWithProperties)doWith.getValue();
                  beanName = doWithValue.getBeanName();
               } while(!StringUtils.isNotBlank(beanName));

               Object outputSource = SpringContext.getBean(beanName);
               Set<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(outputSource.getClass(), new Predicate[]{(clazz) -> {
                  return clazz.equals(OutputSource.class);
               }});
               if (allInterfaces.size() == 0) {
                  throw new RuntimeException(outputSource.getClass().getName() + " is not implements " + OutputSource.class.getName());
               }

               log.info("[Source Info] beanName: " + beanName + " getInputSourceName: " + doWithValue.getInputSourceName() + " outputSourceName: " + outputSource.getClass().getName());
               List<String> keys = (List)this.rabbitConsumerKeys.get(doWithValue.getInputSourceName());
               if (ObjectUtil.isNotNull(keys)) {
                  Iterator var187 = keys.iterator();

                  while(var187.hasNext()) {
                     String key = (String)var187.next();
                     InPutRun inPutRun = new InPutRun((OutputSource)outputSource, key);
                     log.info((String)doWith.getKey() + "_" + key + " InPutRun");
                     TimeTaskManage.createNewTask((String)doWith.getKey() + "_" + key, inPutRun);
                     TimeUnit.SECONDS.sleep(5L);
                  }
               } else {
                  InPutRun inPutRun = new InPutRun((OutputSource)outputSource, doWithValue.getInputSourceName());
                  log.info((String)doWith.getKey() + " InPutRun");
                  TimeTaskManage.createNewTask((String)doWith.getKey() + "_DoWith", inPutRun);
                  TimeUnit.SECONDS.sleep(5L);
               }
            }
         }
      }
   }
}
