package org.com.openmarket.items.core.domain.interfaces.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUtils {
    String getExtension(MultipartFile multipartFile) throws IOException;
}
