# Kubernetes API 
-> Used to read and write Kubernetes resource objects(pod/deamonset/cron everything)
-> Operations are create , update (replace/patch), read , delete 
```shell
POST /api/v1/namespaces/{namespace}/pods
DELETE /api/v1/namespaces/{namespace}/pods/{name}
POST /api/v1/namespaces/{namespace}/services
POST /api/v1/namespaces/{namespace}/configmaps
POST /apis/apps/v1/namespaces/{namespace}/statefulsets
POST /apis/apiextensions.k8s.io/v1beta1/customresourcedefinitions

```
  Example payload for 
```shell
{
  "kind": "StatefulSet",
  "apiVersion": "apps/v1beta1",
  ...
}
```

# Resource Objects 
-> mainly 3 components
#### Resource ObjectMeta: 
      This is metadata about the resource, such as its name, type, api version, annotations, and labels. This contains fields that maybe updated both by the end user and the system (e.g. annotations).
#### ResourceSpec:
       This is defined by the user and describes the desired state of system. Fill this in when creating or updating an object.
#### ResourceStatus:
       This is filled in by the server and reports the current state of the system. In most cases, users don't need to change this.

# CustomResource
-> A resource is an endpoint in k8s API that allow you to store an API object of any kind  
-> A custom resource allows you to create your own API objects and define your own kind just like Pod, Deployment, ReplicaSet, etc
-> 
-> CR is an objects are created from CRDs that have been added to the cluster by a cluster administrator
-> CR allows all cluster users to add the new resource type

-> custom resource created is also stored in the etcd cluster

# Adding CR 
-> CRD without programming 
-> API Aggregation requires programming

# Custom Resource Definition 
-> enables users to add their own/custom objects to the Kubernetes cluster
-> Defines CR
->  CRD object must be a valid DNS subdomain name
-> Custom Resource Definition (CRD) object defines a new, unique object Kind(CR)
   in the cluster and lets the Kubernetes API server handle its entire lifecycle
-> When a cluster administrator adds a new CRD to the cluster, 
   the Kubernetes API server reacts by creating a new RESTful resource path
    _/apis/<spec:group>/<spec:version>/<scope>/*/<names-plural>/..._

# Example 
-> create Kafkatopics CRD 
-> crete some KT 

# Access API

-> follow https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.21/#customresourcedefinition-v1-apiextensions-k8s-io
-> 
```shell

kubectl api-versions 

kubectl proxy --port=8080 &
curl http://localhost:8080/api/

http://localhost:8080/apis/ <- after CRD we should see our domain here

#get services in cluster 
curl http://localhost:8080/api/v1/namespaces/default/endpoints

# get CRD is k8s cluster 
curl http://localhost:8080/apis/apiextensions.k8s.io/v1/customresourcedefinitions

```

```shell
[root@localhost local_cluster_setup]# kubectl get crd
NAME                           CREATED AT
kafkatopics.test.prakash.com   2021-04-17T07:10:26Z
[root@localhost local_cluster_setup]# kubectl get kt
NAME                           AGE
cruise-control-metrics-topic   29m
[root@localhost local_cluster_setup]# curl http://localhost:8080/apis/test.prakash.com/v1alpha1/namespaces/default/kafkatopics/cruise-control-metrics-topic
{"apiVersion":"test.prakash.com/v1alpha1",
"kind":"KafkaTopic",
"metadata":
{"annotations":{
  "kubectl.kubernetes.io/last-applied-configuration":"{\"apiVersion\":\"test.prakash.com/v1alpha1\",\"kind\":\"KafkaTopic\",\"metadata\":{\"annotations\":{},\"name\":\"cruise-control-metrics-topic\",\"namespace\":\"default\"},\"spec\":{\"cluster\":\"DC1-dev-shared-app\",\"name\":\"__CruiseControlMetrics\",\"partition\":\"3\",\"replication\":\"5\"}}\n"},
  "creationTimestamp":"2021-04-17T07:11:22Z",
  "generation":1,"managedFields":[{"apiVersion":"test.prakash.com/v1alpha1","fieldsType":"FieldsV1",
  "fieldsV1":{"f:metadata":{"f:annotations":{".":{},"f:kubectl.kubernetes.io/last-applied-configuration":{}}},"f:spec":{".":{},"f:cluster":{},"f:name":{},"f:partition":{},"f:replication":{}}},"manager":"kubectl-client-side-apply","operation":"Update","time":"2021-04-17T07:11:22Z"}],"name":"cruise-control-metrics-topic","namespace":"default","resourceVersion":"4907","uid":"9e6e9d73-1599-477e-98f7-de767c961371"},
  "spec":{"cluster":"DC1-dev-shared-app","name":"__CruiseControlMetrics","partition":"3","replication":"5"}}
```

Once CRD is applied Kubernetes API created for CustomResourceDefinition .
Next time when you create CR you can use either kubectl or endpoint 
