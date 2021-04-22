package com.prakash.k8s;

import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class K8sOperator {
    public static void main(String[] args) {
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
            SharedInformerFactory sharedInformerFactory = client.informers();
            // Create instance for KafkaTopic Informer
            SharedIndexInformer<KafkaTopic> KafkaTopicInformer = (SharedIndexInformer<KafkaTopic>) sharedInformerFactory.sharedIndexInformerForCustomResource(KafkaTopic.class, 30 * 1000L);
            System.out.println("Informer factory initialized.");
            // Add Event Handler for actions on all KafkaTopic events received
            KafkaTopicInformer.addEventHandler(
                    new ResourceEventHandler<KafkaTopic>() {
                        @Override
                        public void onAdd(KafkaTopic KafkaTopic) {
                            System.out.println("KafkaTopic " + KafkaTopic.getMetadata().getName() + " got added");
                        }

                        @Override
                        public void onUpdate(KafkaTopic oldKafkaTopic, KafkaTopic newKafkaTopic) {
                            System.out.println("KafkaTopic " + oldKafkaTopic.getMetadata().getName() + " got updated");
                        }

                        @Override
                        public void onDelete(KafkaTopic KafkaTopic, boolean deletedFinalStateUnknown) {
                            System.out.println("KafkaTopic " + KafkaTopic.getMetadata().getName() + " got deleted");
                        }
                    }
            );

            System.out.println("Starting all registered informers");
            sharedInformerFactory.startAllRegisteredInformers();

            // Wait for 1 minute
            Thread.sleep(60 * 1000L);

        } catch (KubernetesClientException | InterruptedException ex) {
            // Handle exception
            ex.printStackTrace();
        }
    }
}
