### Setting up
1. Set your projectId and GCP credential locations in `resources/application.properties`  
2. Create test bucket with `gsutil mb gs://gcp-upload-demo`

### Check app in JVM mode 

```shell script
./mvnw compile quarkus:dev
```
OR
```shell script
./mvnw package \
&& java -jar target/quarkus-gcp-storage-write-error-1.0.0-SNAPSHOT-runner.jar
```

Result: new object is created in Storage

### Reproducing issue in native mode

```shell script
./mvnw package -Dquarkus.package.type=uber-jar \
&& target/quarkus-gcp-storage-write-error-1.0.0-SNAPSHOT-runner
```

Output:
```
ERROR [io.qua.run.Application] (main) Failed to start application (with profile prod): com.google.api.client.googleapis.json.GoogleJsonResponseException: 400 Bad Request
{
  "code" : 400,
  "errors" : [ {
    "domain" : "global",
    "message" : "Unsupported content with type: application/octet-stream",
    "reason" : "badContent"
  } ],
  "message" : "Unsupported content with type: application/octet-stream"
}
        at com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest.newExceptionOnError(AbstractGoogleJsonClientRequest.java:113)
        at com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest.newExceptionOnError(AbstractGoogleJsonClientRequest.java:40)
        at com.google.api.client.googleapis.services.AbstractGoogleClientRequest.executeUnparsed(AbstractGoogleClientRequest.java:555)
        at com.google.api.client.googleapis.services.AbstractGoogleClientRequest.executeUnparsed(AbstractGoogleClientRequest.java:475)
        at com.google.api.client.googleapis.services.AbstractGoogleClientRequest.execute(AbstractGoogleClientRequest.java:592)
        at com.google.cloud.storage.spi.v1.HttpStorageRpc.create(HttpStorageRpc.java:305)
        at com.google.cloud.storage.StorageImpl$3.call(StorageImpl.java:213)
        at com.google.cloud.storage.StorageImpl$3.call(StorageImpl.java:210)
        at com.google.api.gax.retrying.DirectRetryingExecutor.submit(DirectRetryingExecutor.java:105)
        at com.google.cloud.RetryHelper.run(RetryHelper.java:76)
        at com.google.cloud.RetryHelper.runWithRetries(RetryHelper.java:50)
        at com.google.cloud.storage.StorageImpl.internalCreate(StorageImpl.java:209)
        at com.google.cloud.storage.StorageImpl.create(StorageImpl.java:171)
        at com.github.medvedev.HelloCommando.run(HelloCommando.java:23)
        at com.github.medvedev.HelloCommando_ClientProxy.run(HelloCommando_ClientProxy.zig:157)
        at io.quarkus.runtime.ApplicationLifecycleManager.run(ApplicationLifecycleManager.java:117)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:62)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:38)
        at io.quarkus.runner.GeneratedMain.main(GeneratedMain.zig:30)

```
