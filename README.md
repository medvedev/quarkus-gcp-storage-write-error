### Setting up
Set your projectId and GCP credentials file location in `resources/application.properties`  

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
./mvnw package ./mvnw clean package -Pnative \
&& target/quarkus-gcp-storage-write-error-1.0.0-SNAPSHOT-runner
```

Output:
```
Creating bucket: upload-demo-0959
Uploading byte[]....
com.google.cloud.storage.StorageException: Unsupported content with type: application/octet-stream
        at com.google.cloud.storage.spi.v1.HttpStorageRpc.translate(HttpStorageRpc.java:227)
        at com.google.cloud.storage.spi.v1.HttpStorageRpc.create(HttpStorageRpc.java:308)
        at com.google.cloud.storage.StorageImpl$3.call(StorageImpl.java:213)
        at com.google.cloud.storage.StorageImpl$3.call(StorageImpl.java:210)
        at com.google.api.gax.retrying.DirectRetryingExecutor.submit(DirectRetryingExecutor.java:105)
        at com.google.cloud.RetryHelper.run(RetryHelper.java:76)
        at com.google.cloud.RetryHelper.runWithRetries(RetryHelper.java:50)
        at com.google.cloud.storage.StorageImpl.internalCreate(StorageImpl.java:209)
        at com.google.cloud.storage.StorageImpl.create(StorageImpl.java:171)
        at com.github.medvedev.HelloCommando.uploadAsBytes(HelloCommando.java:52)
        at com.github.medvedev.HelloCommando.run(HelloCommando.java:26)
        at com.github.medvedev.HelloCommando_ClientProxy.run(HelloCommando_ClientProxy.zig:157)
        at io.quarkus.runtime.ApplicationLifecycleManager.run(ApplicationLifecycleManager.java:117)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:62)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:38)
        at io.quarkus.runner.GeneratedMain.main(GeneratedMain.zig:30)
Caused by: com.google.api.client.googleapis.json.GoogleJsonResponseException: 400 Bad Request
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
        ... 14 more
Uploading from file....
com.google.cloud.storage.StorageException: java.lang.IllegalArgumentException: java.net.MalformedURLException
        at com.google.cloud.storage.BlobWriteChannel.flushBuffer(BlobWriteChannel.java:77)
        at com.google.cloud.BaseWriteChannel.close(BaseWriteChannel.java:151)
        at com.google.cloud.storage.StorageImpl.createFrom(StorageImpl.java:256)
        at com.google.cloud.storage.StorageImpl.createFrom(StorageImpl.java:237)
        at com.google.cloud.storage.StorageImpl.createFrom(StorageImpl.java:227)
        at com.github.medvedev.HelloCommando.uploadAsFile(HelloCommando.java:42)
        at com.github.medvedev.HelloCommando.run(HelloCommando.java:27)
        at com.github.medvedev.HelloCommando_ClientProxy.run(HelloCommando_ClientProxy.zig:157)
        at io.quarkus.runtime.ApplicationLifecycleManager.run(ApplicationLifecycleManager.java:117)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:62)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:38)
        at io.quarkus.runner.GeneratedMain.main(GeneratedMain.zig:30)
Caused by: java.lang.IllegalArgumentException: java.net.MalformedURLException
        at com.google.api.client.http.GenericUrl.parseURL(GenericUrl.java:682)
        at com.google.api.client.http.GenericUrl.<init>(GenericUrl.java:126)
        at com.google.api.client.http.GenericUrl.<init>(GenericUrl.java:109)
        at com.google.cloud.storage.spi.v1.HttpStorageRpc.writeWithResponse(HttpStorageRpc.java:746)
        at com.google.cloud.storage.BlobWriteChannel$1.run(BlobWriteChannel.java:69)
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
        at com.google.api.gax.retrying.DirectRetryingExecutor.submit(DirectRetryingExecutor.java:105)
        at com.google.cloud.RetryHelper.run(RetryHelper.java:76)
        at com.google.cloud.RetryHelper.runWithRetries(RetryHelper.java:50)
        at com.google.cloud.storage.BlobWriteChannel.flushBuffer(BlobWriteChannel.java:61)
        ... 11 more
Caused by: java.net.MalformedURLException
        at java.net.URL.<init>(URL.java:679)
        at java.net.URL.<init>(URL.java:541)
        at java.net.URL.<init>(URL.java:488)
        at com.google.api.client.http.GenericUrl.parseURL(GenericUrl.java:680)
        ... 20 more
Caused by: java.lang.NullPointerException
        at java.net.URL.<init>(URL.java:585)
        ... 23 more
```
