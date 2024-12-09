package org.com.openmarket.items.core.domain.usecase.item.uploadImage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.items.core.domain.interfaces.uploader.ImageUploaderRepository;
import org.com.openmarket.items.core.domain.interfaces.utils.FileUtils;
import org.com.openmarket.items.core.domain.usecase.item.uploadImage.exception.ErrorWhileConvertingMultipartFileException;
import org.com.openmarket.items.core.domain.usecase.item.uploadImage.exception.InvalidExtensionException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UploadImageUsecase {
    private final static String TEMP_FOLDER = "TEMP_DIR_PATH";
    private final static String REPOSITORY_NAME = "REPOSITORY_NAME_ITEMS_IMAGES";
    private final static List<String> VALID_EXTENSIONS = List.of("image/jpg", "image/jpeg", "image/png", "jpg", "jpeg", "png");

    private final ImageUploaderRepository imageUploaderRepository;

    private final FileUtils fileUtils;

    public String execute(MultipartFile multipartFile) {
        File file;

        checkTempFolderExists();
        checkValidExtension(multipartFile);

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

    private void checkTempFolderExists() {
        File tempFolder = new File(System.getenv(TEMP_FOLDER));

        if (!tempFolder.exists()) {
            throw new ErrorWhileConvertingMultipartFileException("Temp folder does not exists.");
        }
    }

    private void checkValidExtension(MultipartFile multipartFile) {
        String extension;

        try {
            extension = fileUtils.getExtension(multipartFile);
        } catch (IOException exception) {
            String message = "Error while converting MultipartFile. Could not specify file extension!";
            log.error("{} Error: {}", message, exception.getMessage());
            throw new ErrorWhileConvertingMultipartFileException(message);
        }

        if (!VALID_EXTENSIONS.contains(extension)) {
            throw new InvalidExtensionException();
        }
    }

    private File multipartToFile(MultipartFile multipart, String fileName) throws IOException {
        File convFile = new File(System.getenv(TEMP_FOLDER) + "/" + fileName);
        multipart.transferTo(convFile);
        return convFile;
    }

    private String uploadImage(File file) {
        String fileName = UUID.randomUUID().toString().concat("_").concat(file.getName());
        return imageUploaderRepository.upload(System.getenv(REPOSITORY_NAME), fileName, file);
    }
}
