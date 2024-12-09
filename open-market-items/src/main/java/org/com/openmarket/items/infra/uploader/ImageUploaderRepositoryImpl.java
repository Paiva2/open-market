package org.com.openmarket.items.infra.uploader;

import com.amazonaws.services.s3.AmazonS3;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.interfaces.uploader.ImageUploaderRepository;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@AllArgsConstructor
public class ImageUploaderRepositoryImpl implements ImageUploaderRepository {
    private final AmazonS3 s3Client;

    public String upload(String bucketName, String key, File file) {
        s3Client.putObject(bucketName, key, file);
        return mountDocumentUrl(bucketName, key);
    }

    private String mountDocumentUrl(String destination, String fileName) {
        return "https://" + destination + ".s3.us-east-1.amazonaws.com/" + fileName;
    }
}
