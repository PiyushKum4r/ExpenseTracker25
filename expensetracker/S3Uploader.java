package com.expensetracker;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.file.Paths;

public class S3Uploader {

    private final S3Client s3;
    private final String bucketName;

    public S3Uploader(String accessKey, String secretKey, String region, String bucketName) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        this.bucketName = bucketName;
    }

    public void uploadFile(String keyName, String filePath) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            s3.putObject(request, Paths.get(filePath));
            System.out.println("✅ File uploaded to S3: " + keyName);
        } catch (S3Exception e) {
            System.err.println("❌ Upload failed: " + e.awsErrorDetails().errorMessage());
        }
    }

    public void close() {
        s3.close();
    }
}

