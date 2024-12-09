package org.com.openmarket.items.infra.utils;

import org.apache.tika.Tika;
import org.com.openmarket.items.core.domain.interfaces.utils.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FileUtilsImpl implements FileUtils {
    public static final Tika tika = new Tika();

    @Override
    public String getExtension(MultipartFile multipartFile) throws IOException {
        return tika.detect(multipartFile.getBytes());
    }
}
