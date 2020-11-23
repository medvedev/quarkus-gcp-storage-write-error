package com.github.medvedev;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;

@QuarkusMain
public class HelloCommando implements QuarkusApplication {

    @Inject
    Storage storage;

    @Override
    public int run(String... args) throws Exception {
        BlobInfo blobInfo = BlobInfo.newBuilder("gcp-upload-demo", "test-upload").build();
        storage.create(blobInfo, "test".getBytes());
        return 0;
    }
}
