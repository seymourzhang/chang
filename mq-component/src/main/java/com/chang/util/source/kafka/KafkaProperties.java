package com.chang.util.source.kafka;

import java.util.HashMap;
import java.util.Map;

public class KafkaProperties {
    private String servers;
    private String groupId;
    private String topic;
    private String messageType = "str";
    private Integer pollMinRecords;
    private Integer pollMaxRecords;
    private Integer pollTimeout;
    private Map<String, Object> parm = new HashMap();

    public String getServers() {
        return this.servers;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public Map<String, Object> getParm() {
        return this.parm;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setParm(Map<String, Object> parm) {
        this.parm = parm;
    }

    public Integer getPollMinRecords() {
        return pollMinRecords;
    }

    public void setPollMinRecords(Integer pollMinRecords) {
        this.pollMinRecords = pollMinRecords;
    }

    public Integer getPollMaxRecords() {
        return pollMaxRecords;
    }

    public void setPollMaxRecords(Integer pollMaxRecords) {
        this.pollMaxRecords = pollMaxRecords;
    }

    public Integer getPollTimeout() {
        return pollTimeout;
    }

    public void setPollTimeout(Integer pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof KafkaProperties)) {
            return false;
        } else {
            KafkaProperties other = (KafkaProperties) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71:
                {
                    Object this$servers = this.getServers();
                    Object other$servers = other.getServers();
                    if (this$servers == null) {
                        if (other$servers == null) {
                            break label71;
                        }
                    } else if (this$servers.equals(other$servers)) {
                        break label71;
                    }

                    return false;
                }

                Object this$groupId = this.getGroupId();
                Object other$groupId = other.getGroupId();
                if (this$groupId == null) {
                    if (other$groupId != null) {
                        return false;
                    }
                } else if (!this$groupId.equals(other$groupId)) {
                    return false;
                }

                label57:
                {
                    Object this$topic = this.getTopic();
                    Object other$topic = other.getTopic();
                    if (this$topic == null) {
                        if (other$topic == null) {
                            break label57;
                        }
                    } else if (this$topic.equals(other$topic)) {
                        break label57;
                    }

                    return false;
                }

                Object this$messageType = this.getMessageType();
                Object other$messageType = other.getMessageType();
                if (this$messageType == null) {
                    if (other$messageType != null) {
                        return false;
                    }
                } else if (!this$messageType.equals(other$messageType)) {
                    return false;
                }

                Object this$parm = this.getParm();
                Object other$parm = other.getParm();
                if (this$parm == null) {
                    if (other$parm == null) {
                        return true;
                    }
                } else if (this$parm.equals(other$parm)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof KafkaProperties;
    }

    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        Object $servers = this.getServers();
        result = result * 59 + ($servers == null ? 43 : $servers.hashCode());
        Object $groupId = this.getGroupId();
        result = result * 59 + ($groupId == null ? 43 : $groupId.hashCode());
        Object $topic = this.getTopic();
        result = result * 59 + ($topic == null ? 43 : $topic.hashCode());
        Object $messageType = this.getMessageType();
        result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
        Object $parm = this.getParm();
        result = result * 59 + ($parm == null ? 43 : $parm.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "KafkaProperties{" +
            "servers='" + servers + '\'' +
            ", groupId='" + groupId + '\'' +
            ", topic='" + topic + '\'' +
            ", messageType='" + messageType + '\'' +
            ", pollMinRecords=" + pollMinRecords +
            ", pollMaxRecords=" + pollMaxRecords +
            ", pollTimeout=" + pollTimeout +
            ", parm=" + parm +
            '}';
    }
}
