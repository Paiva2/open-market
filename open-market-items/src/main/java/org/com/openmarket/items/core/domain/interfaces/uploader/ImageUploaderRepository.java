package org.com.openmarket.items.core.domain.interfaces.uploader;

import java.io.File;

public interface ImageUploaderRepository {
    String upload(String bucketName, String key, File file);
}
