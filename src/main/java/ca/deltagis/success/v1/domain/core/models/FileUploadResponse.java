package ca.deltagis.success.v1.domain.core.models;

import ca.deltagis.success.v1.adapters.web.message.files.FileUploadError;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-04 07:02:14
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/core/models/FileUploadResponse.java
 * @Description: Represents the response of a file upload.
 */
@Data
@NoArgsConstructor
public class FileUploadResponse {
    private String fileName;

    private String fileDownloadUri;

    private String fileType;

    private long size;

    private FileUploadError error;

    private String errorDescription;
    
    public FileUploadResponse(String fileName, FileUploadError error) {
        this.fileName = fileName;
        this.error = error;
    }

    public FileUploadResponse(String fileName, FileUploadError error, String errorDescription) {
        this.fileName = fileName;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public FileUploadResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}
