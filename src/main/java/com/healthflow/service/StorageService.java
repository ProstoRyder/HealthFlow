package com.healthflow.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StorageService {

    StoredFile uploadAvatar(MultipartFile file, String folder, UUID ownerId);

    void deleteFile(String key);

    record StoredFile(String key, String url) {}
}
