package com.github.medvedev;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@QuarkusMain
public class HelloCommando implements QuarkusApplication {

    @Inject
    Storage storage;

    @Override
    public int run(String... args) throws Exception {
        String bucketName = args.length > 0 ? args[0] : "upload-demo-" + RandomStringUtils.randomNumeric(4);
        createBucket(bucketName);
        uploadAsBytes(bucketName);
        uploadAsFile(bucketName);
        System.out.println("To delete bucket  run:\n" +
                "gsutil rm -r gs://" + bucketName);
        return 0;
    }

    private void uploadAsFile(String bucketName) throws IOException {
        BlobInfo blobInfo;

        Path tempFile = Files.createTempFile("tmp-prfx", "tmp-sffx");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()));
        writer.write("test", 0, 4);
        writer.close();

        blobInfo = BlobInfo.newBuilder(bucketName, "uploadAsFile").build();
        System.out.println("Uploading from file....");
        try {
            storage.createFrom(blobInfo, tempFile);
        } catch (Exception e) {
            e.printStackTrace();
        };
    }

    private void uploadAsBytes(String bucketName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, "uploadAsBytes").build();
        System.out.println("Uploading byte[]....");
        try {
            storage.create(blobInfo, "test".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createBucket(String bucketName) {
        System.out.println("Creating bucket: " + bucketName);
        storage.create(
                BucketInfo.newBuilder(bucketName)
                        .setStorageClass(StorageClass.COLDLINE)
                        .setLocation("EU")
                        .build());
    }

}
