package com.prakash.k8s;

public class KafkaTopicSpec {
    private String name;
    private String partition;
    private String replication;
    private String cluster;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getReplication() {
        return replication;
    }

    public void setReplication(String replication) {
        this.replication = replication;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}
