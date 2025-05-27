//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;
//
//import java.net.URI;
//import java.time.Duration;
//
//public String generatePresignedUrl(String bucketName, String key, Duration duration) {
//    try (S3Presigner presigner = S3Presigner.builder()
//            .endpointOverride(URI.create("https://" + accountId + ".r2.cloudflarestorage.com"))
//            .region(Region.of("auto"))
//            .credentialsProvider(StaticCredentialsProvider.create(
//                    AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
//            .build()) {
//
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .build();
//
//        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .getObjectRequest(getObjectRequest)
//                .signatureDuration(duration)
//                .build();
//
//        return presigner.presignGetObject(presignRequest).url().toString();
//    }
//}
