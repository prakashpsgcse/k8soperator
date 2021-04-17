package com.prakash.k8s;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

public class KafkaTopicOperator {
    public static void main(String[] args) {
        Config config = new ConfigBuilder()
                .withMasterUrl("http://localhost:8080")
                .build();
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {

          KafkaTopic cruiseControlTopic=new KafkaTopic();
          cruiseControlTopic.setMetadata(new ObjectMetaBuilder().withName("cc-topic").build());
          KafkaTopicSpec spec=new KafkaTopicSpec();
          spec.setCluster("localhost:9092");
          spec.setName("test-topic");
          spec.setPartition("5");
          spec.setReplication("3");
            cruiseControlTopic.setSpec(spec);


            MixedOperation<KafkaTopic, KubernetesResourceList<KafkaTopic>, Resource<KafkaTopic>> topicClient = client.customResources(KafkaTopic.class);

            topicClient.inNamespace("default").createOrReplace(cruiseControlTopic);

            // List All Dummies in default namespace
            KubernetesResourceList<KafkaTopic> topicList = topicClient.inNamespace("default").list();
            topicList.getItems().forEach(topic->System.out.println(topic));

        } catch (KubernetesClientException ex) {
            // Handle exception
            ex.printStackTrace();
        }
    }
}
