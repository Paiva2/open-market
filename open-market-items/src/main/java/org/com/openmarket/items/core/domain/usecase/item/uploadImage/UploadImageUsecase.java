package org.com.openmarket.items.core.domain.usecase.item.uploadImage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.items.core.domain.interfaces.uploader.ImageUploaderRepository;
import org.com.openmarket.items.core.domain.usecase.item.uploadImage.exception.ErrorWhileConvertingMultipartFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UploadImageUsecase {
    private final static String BUCKET_NAME = "open-market-items-images";

    private final ImageUploaderRepository imageUploaderRepository;

    public String execute(MultipartFile multipartFile) {
        File file;

        try {
            file = multipartToFile(multipartFile, multipartFile.getOriginalFilename());
        } catch (IOException exception) {
            String message = "Error while converting MultipartFile...";
            log.error("{} Error: {}", message, exception.getMessage());
            throw new ErrorWhileConvertingMultipartFileException(message);
        }

        String url = uploadImage(file);

        if (file.exists()) {
            file.delete();
        }

        return url;
    }

    private File multipartToFile(MultipartFile multipart, String fileName) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    private String uploadImage(File file) {
        String fileName = UUID.randomUUID().toString().concat("_").concat(file.getName());

        return imageUploaderRepository.upload(BUCKET_NAME, fileName, file);
    }
}
