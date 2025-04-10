
package com.erastedev.ciexplore.v1.application.validator.out.uploader;

import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-03 20:23:26
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/application/validator/out/uploader/FileValidator.java
 * @Description: This class validates the file to be uploaded. It verifies that the file name is not empty and does not contain invalid sequences of characters.
 */
@Service
public class FileValidator {
    /**
     * Validates the given file to be uploaded. The validation checks that the file name is not empty and does not contain invalid sequences of characters.
     *
     * @param file the file to be validated
     * @return null if the validation succeeds, otherwise the error message
     */
    public FileUploadError validate(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName == null || file.isEmpty()) {
            return FileUploadError.EMPTY_FILE;
        }

        if (fileName.contains("..")) {
            return FileUploadError.INVALID_FILE_NAME;
        }

        return null;
    }
}
