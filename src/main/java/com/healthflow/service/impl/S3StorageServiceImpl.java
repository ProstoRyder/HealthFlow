package com.healthflow.service.impl;

import com.healthflow.common.BadRequestException;
import com.healthflow.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class S3StorageServiceImpl implements StorageService {

    private static final long MAX_AVATAR_SIZE_BYTES = 5L * 1024L * 1024L;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final S3Client s3Client;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3StorageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public StoredFile uploadAvatar(MultipartFile file, String folder, UUID ownerId) {
        validateFile(file);
        String key = buildObjectKey(file, folder, ownerId);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            return new StoredFile(key, buildObjectUrl(key));
        } catch (IOException exception) {
            throw new BadRequestException("Failed to read uploaded file");
        } catch (Exception exception) {
            throw new BadRequestException("Failed to upload file to S3");
        }
    }

    @Override
    public void deleteFile(String key) {
        if (key == null || key.isBlank()) {
            return;
        }

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
        } catch (Exception exception) {
            throw new BadRequestException("Failed to delete file from S3");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Avatar file is required");
        }

        if (file.getSize() > MAX_AVATAR_SIZE_BYTES) {
            throw new BadRequestException("Avatar file size must not exceed 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BadRequestException("Only JPEG, PNG and WEBP images are allowed");
        }
    }

    private String buildObjectKey(MultipartFile file, String folder, UUID ownerId) {
        String extension = extractExtension(file.getOriginalFilename());
        return folder + "/" + ownerId + "/" + UUID.randomUUID() + "." + extension;
    }

    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "bin";
        }

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        if (extension.isBlank()) {
            return "bin";
        }

        return extension;
    }

    private String buildObjectUrl(String key) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + key;
    }
}
