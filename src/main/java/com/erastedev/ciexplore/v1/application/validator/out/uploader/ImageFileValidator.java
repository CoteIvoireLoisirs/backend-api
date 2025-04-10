package com.erastedev.ciexplore.v1.application.validator.out.uploader;

import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-03 21:46:02
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/application/validator/out/uploader/ImageFileValidator.java
 * @Description: This class validates the image file to be uploaded. It checks that the file is not empty, that it is an image file, and that it does not exceed a certain size.
 */
@Component
public class ImageFileValidator {
    /**
     * Max file size in bytes (10MB)
     */
    private static final long MAX_FILE_SIZE = 1000 * 1024 * 10;

    /**
     * Validate the given image file. The validation checks that the file is not empty, that it is an image file, and that it does not exceed a certain size.
     *
     * @param file the image file to be validated
     * @throws IllegalArgumentException if the validation fails
     */
    public FileUploadError validate(MultipartFile file) {
        // Empty validation
        if (file == null || file.isEmpty()) {
            // throw new IllegalArgumentException("No images selected!");
            return FileUploadError.EMPTY_FILE;
        }

        // Type validation
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            // throw new IllegalArgumentException("The file is not a valid image!");
            return FileUploadError.INVALID_FILE_TYPE;
        }

        // Size validation
        if (file.getSize() > MAX_FILE_SIZE) {
            // throw new IllegalArgumentException("The image exceeds the maximum allowed size (1 MB).");
            return FileUploadError.FILE_SIZE_EXCEEDED;
        }

        // Extension validation
        if (!Objects.requireNonNull(file.getOriginalFilename()).matches("(?i).+\\.(png|jpg|jpeg|gif|bmp|tiff|tif|svg|webp|ico)$")) {
            // throw new RuntimeException("The file is not a valid image.");
            return FileUploadError.INVALID_FILE_TYPE;
        }

        // Check path sequence (security)
        if (file.getOriginalFilename().contains("..")) {
            // throw new RuntimeException("Invalid file path sequence : " + file.getOriginalFilename());
            return FileUploadError.INVALID_FILE_NAME;
        }

        // no error, file is valid
        return null;
    }
}
