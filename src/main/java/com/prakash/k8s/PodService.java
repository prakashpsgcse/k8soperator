package com.prakash.k8s;

import io.fabric8.kubernetes.client.*;

public class PodService {

    public static void main(String[] args) {
        Config config = new ConfigBuilder()
                .withMasterUrl("http://localhost:8080")
                .build();
        try (KubernetesClient client = new DefaultKubernetesClient(config)) {

            client.pods().inNamespace("default").list().getItems().forEach(
                    pod -> System.out.println(pod.getMetadata().getName())
            );

        } catch (KubernetesClientException ex) {
            // Handle exception
            ex.printStackTrace();
        }
    }
    public static void getClient(){
    }
    public static void getPods(){
    }
}
