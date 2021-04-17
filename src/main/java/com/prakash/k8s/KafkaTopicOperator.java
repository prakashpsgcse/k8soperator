package com.prakash.k8s;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class KafkaTopicOperator {

    public static void method1WithPOJOS(){
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


    public static void method1WithOUTPOJOS(){
        Config config = new ConfigBuilder()
                .withMasterUrl("http://localhost:8080")
                .build();
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {

            CustomResourceDefinitionContext context = new CustomResourceDefinitionContext
                    .Builder()
                    .withGroup("test.prakash.com")
                    .withKind("KafkaTopic")
                    .withName("kafkatopics.test.prakash.com")
                    .withPlural("kafkatopics")
                    .withScope("Namespaced")
                    .withVersion("v1alpha1")
                    .build();
            FileInputStream fin=new FileInputStream("/home/prakash/Documents/git/k8soperator/CustomResource/CruiseControlTopic.yaml");

            Map<String, Object> topic = client.customResource(context)
                    .load(fin);

            client.customResource(context).create("default", topic);
            System.out.println("Created!");

            Map<String, Object> list = client.customResource(context).list("default");
            System.out.printf("%d items found in default namespace\n", ((List<Object>)list.get("items")).size());

        } catch (KubernetesClientException | IOException ex) {
            // Handle exception
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        method1WithOUTPOJOS();
    }
}
