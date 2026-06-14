package com.chang.util.source.kafka;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.chang.util.source.InputSource;
import com.chang.util.source.OutputSource;
import com.chang.util.source.common.ManageStatistics;
import com.chang.util.source.common.SourceContext;
import com.chang.util.source.common.Util;
import com.chang.util.source.configuration.api.KafkaFactory;
import com.chang.util.source.configuration.api.KafkaOptions;
import com.sun.management.OperatingSystemMXBean;
import com.chang.until.timeTaskApi.TimeTaskManage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class KafkaInput implements InputSource<JSONObject, Object> {
    private static final Logger log = LoggerFactory.getLogger(KafkaInput.class);
    private KafkaConsumer<String, byte[]> consumer;
    private KafkaOptions kafkaOptions;
    private final String messageType;
    private Map<String, Object> parm;
    private final String keyName;
    private double lastError = 0;
    private double integral = 0;
    private final double Kp = 0.5; // 比例系数
    private final double Ki = 0.1; // 积分系数
    private final double Kd = 0.2; // 微分系数
    private final double targetCpu = 0.6; // 目标CPU使用率(60%)

    public KafkaInput(String servers, String groupId,
                      String topic, String keyName,
                      String messageType, Map<String, Object> parm,
                      Integer pollMinRecords, Integer pollMaxRecords, Integer pollTimeout) {
        this.messageType = messageType;
        this.parm = parm;
        this.keyName = keyName;
        this.kafkaOptions = KafkaOptions.builder()
            .servers(servers).groupId(groupId)
            .topic(topic).keyName(keyName)
            .pollMinRecords(pollMinRecords)
            .pollMaxRecords(pollMaxRecords)
            .pollTimeout(pollTimeout)
            .build();
        this.consumer = KafkaFactory.getConsumer(this.kafkaOptions);
    }

    public long calculateSleepTime(double currentCpu, Integer maxSleepTime) {
        double error = currentCpu - targetCpu;
        integral += error;
        double derivative = error - lastError;
        lastError = error;

        double output = Kp * error + Ki * integral + Kd * derivative;

        // 将输出转换为sleep时间(毫秒)
        long sleepTime = (long) (Math.abs(output) * 1000);
        return Math.min(sleepTime, maxSleepTime); // 限制最大sleep时间
    }

    public void InPut(final OutputSource source, final Function<JSONObject, Object> function) {
        this.consumer = KafkaFactory.getConsumer(this.kafkaOptions);
        this.consumer.subscribe(Collections.singletonList(this.kafkaOptions.getTopic()));
        try {
            Integer maxNum = KafkaInput.this.kafkaOptions.getPollMaxRecords();
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            TimeTaskManage.createNewTask(this.keyName, new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            try {
                                SourceContext.setExParm(KafkaInput.this.parm);
                                ConsumerRecords<String, byte[]> consumerRecords = KafkaInput.this.consumer.poll(Duration.ofSeconds(1L));
                                // 处理kafka数据
                                Iterator<ConsumerRecord<String, byte[]>> recordIterator = consumerRecords.iterator();
                                while (recordIterator.hasNext()) {
                                    ManageStatistics.inMessageAdd(KafkaInput.this.kafkaOptions.getKeyName());
                                    ConsumerRecord<String, byte[]> record = (ConsumerRecord) recordIterator.next();
                                    String key = (String) record.key();
                                    Object revData = Util.getRevData(KafkaInput.this.messageType, (byte[]) record.value());
                                    long offset = record.offset();
                                    int partition = record.partition();
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("message", revData);
                                    jsonObject.put("offset", offset);
                                    jsonObject.put("partition", partition);
                                    jsonObject.put("key", key);
                                    if (ObjectUtil.isNotNull(function)) {
                                        source.Output(function.apply(jsonObject));
                                    } else {
                                        source.Output(jsonObject);
                                    }
                                }
                                SourceContext.clearExParm();
                                // 对服务器负载情况判断，进行重新计算拉取数量
                                if (!consumerRecords.isEmpty() && KafkaInput.this.kafkaOptions.getPollTimeout() > 0) {
                                    long timeout = KafkaInput.this.kafkaOptions.getPollTimeout();
                                    log.info("kafka 拉取到新数据后休眠时间：{}!~~~~~~~~~~~~~~~~~~~~~", timeout);
                                    Thread.sleep(timeout);
                                    log.info("kafka 拉取到新数据后休眠后记录时间：!··························");
                                    /*if (cpuUsage >= 0.7D) {
                                        // 获取当前CPU使用率
                                        double cpuUsage = osBean.getSystemCpuLoad();
                                        // 记录速率调整情况
                                        log.info("CPU system 使用率: {}", cpuUsage);
                                        // 动态计算sleep时间
//                                        long timeout = calculateSleepTime(cpuUsage, KafkaInput.this.kafkaOptions.getPollTimeout());
                                        log.info("CPU超过负荷: {}. 调整sleep时间: {}ms", cpuUsage, KafkaInput.this.kafkaOptions.getPollTimeout());
                                        Thread.sleep(timeout);
                                        OperatingSystemMXBean osBeanAfter = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
                                        cpuUsage = osBeanAfter.getSystemCpuLoad();
                                        if (cpuUsage >= 0.8D) {
                                            log.info("sleep后CPU依旧超过负荷: {}.", cpuUsage);
                                            Thread.sleep(timeout);
                                            KafkaOptions kafkaOptions = KafkaInput.this.kafkaOptions;
                                            int pollRecords = kafkaOptions.getPollMinRecords();
                                            KafkaInput.this.kafkaOptions = KafkaOptions.builder()
                                                .servers(kafkaOptions.getServers()).groupId(kafkaOptions.getGroupId())
                                                .topic(kafkaOptions.getTopic()).keyName(kafkaOptions.getKeyName())
                                                .pollMaxRecords(maxNum)
                                                .pollMinRecords(pollRecords/2 < 1 ? 1 : pollRecords/2).pollTimeout(kafkaOptions.getPollTimeout())
                                                .build();
                                            TimeTaskManage.stopTask(keyName);
                                            TimeTaskManage.clearDoneTask();
                                            InPut(source, (Function) null);
                                            break;
                                        }
                                    } else if (cpuUsage <= 0.2D) {
                                        // 如何cpu<0.6使用率，加大消费数量
                                        log.info("CPU使用率偏低: {}，需要加大消费数量:{}", cpuUsage, KafkaInput.this.kafkaOptions.getPollMinRecords());
                                        KafkaOptions kafkaOptions = KafkaInput.this.kafkaOptions;
                                        int pollRecords = kafkaOptions.getPollMinRecords();
                                        KafkaInput.this.kafkaOptions = KafkaOptions.builder()
                                            .servers(kafkaOptions.getServers()).groupId(kafkaOptions.getGroupId())
                                            .topic(kafkaOptions.getTopic()).keyName(kafkaOptions.getKeyName())
                                            .pollMaxRecords(maxNum)
                                            .pollMinRecords(pollRecords > maxNum ? maxNum : pollRecords*2).pollTimeout(kafkaOptions.getPollTimeout())
                                            .build();
                                        TimeTaskManage.stopTask(keyName);
                                        TimeTaskManage.clearDoneTask();
                                        InPut(source, (Function) null);
                                        break;
                                    }*/
                                } else {
                                    log.info("kafka 未拉取到新数据!~~~~~~~~~~~~~~~~~~~~~");
                                }
                            } catch (Throwable var10) {
                                KafkaInput.log.error("KafkaInput ERR: ", var10);
                            }
                        }
                    } catch (Throwable var11) {
                        throw var11;
                    }
                }
            });
        } catch (Exception var4) {
            log.error("KafkaInput ERR: ", var4);
        }

    }

    public Map<String, Object> getSourceExParm() {
        return this.parm;
    }

    public void close() {
        TimeTaskManage.stopTask(this.keyName);
        TimeTaskManage.clearDoneTask();
        this.consumer.close();
    }

    protected void finalize() throws Throwable {
        this.close();
    }

    public KafkaOptions getKafkaOptions() {
        return this.kafkaOptions;
    }
}
