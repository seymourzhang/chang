package com.chang.util.source.configuration.api;

public class KafkaOptions {
    private String servers;
    private String groupId;
    private String keyName;
    private String topic;
    private Integer pollMinRecords;
    private Integer pollMaxRecords;
    private Integer pollTimeout;

    public static KafkaOptionsBuilder builder() {
        return new KafkaOptionsBuilder();
    }

    public String getServers() {
        return this.servers;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    @Override
    public String toString() {
        return "KafkaOptions{" +
            "servers='" + servers + '\'' +
            ", groupId='" + groupId + '\'' +
            ", keyName='" + keyName + '\'' +
            ", topic='" + topic + '\'' +
            ", pollMinRecords=" + pollMinRecords +
            ", pollMaxRecords=" + pollMaxRecords +
            ", pollTimeout=" + pollTimeout +
            '}';
    }

    public KafkaOptions(String servers, String groupId, String keyName, String topic, Integer pollMinRecords, Integer pollMaxRecords, Integer pollTimeout) {
        this.servers = servers;
        this.groupId = groupId;
        this.keyName = keyName;
        this.topic = topic;
        this.pollMinRecords = pollMinRecords;
        this.pollMaxRecords = pollMaxRecords;
        this.pollTimeout = pollTimeout;
    }

    public KafkaOptions() {
    }

    public static class KafkaOptionsBuilder {
        private String servers;
        private String groupId;
        private String keyName;
        private String topic;
        private Integer pollMinRecords;
        private Integer pollMaxRecords;
        private Integer pollTimeout;

        KafkaOptionsBuilder() {
        }

        public KafkaOptionsBuilder servers(String servers) {
            this.servers = servers;
            return this;
        }

        public KafkaOptionsBuilder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public KafkaOptionsBuilder keyName(String keyName) {
            this.keyName = keyName;
            return this;
        }

        public KafkaOptionsBuilder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public KafkaOptionsBuilder pollMinRecords(Integer pollMinRecords) {
            this.pollMinRecords = pollMinRecords;
            return this;
        }

        public KafkaOptionsBuilder pollMaxRecords(Integer pollMaxRecords) {
            this.pollMaxRecords = pollMaxRecords;
            return this;
        }


        public KafkaOptionsBuilder pollTimeout(Integer pollTimeout) {
            this.pollTimeout = pollTimeout;
            return this;
        }

        public KafkaOptions build() {
            return new KafkaOptions(this.servers, this.groupId, this.keyName, this.topic, this.pollMinRecords, this.pollMaxRecords, this.pollTimeout);
        }

        public String toString() {
            return "KafkaOptions.KafkaOptionsBuilder(servers=" + this.servers +
                ", groupId=" + this.groupId +
                ", keyName=" + this.keyName +
                ", topic=" + this.topic +
                ", pollMinRecords=" + this.pollMinRecords +
                ", pollMaxRecords=" + this.pollMaxRecords +
                ", pollTimeout=" + this.pollTimeout +
                ")";
        }
    }
}
