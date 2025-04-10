package ca.deltagis.success.v1.application.services.files;

import ca.deltagis.success.v1.domain.core.models.FileNameParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-03 22:16:37
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/application/services/files/FileNameBuilder.java
 * @Description: Builder for file name
 */
@Getter
@NoArgsConstructor
public class FileNameBuilder {
    private MultipartFile file;

    private String newName;

    private String folder;

    private String prefix;

    private String suffix;

    private Logger logger = LoggerFactory.getLogger(FileNameBuilder.class);

    FileNameBuilder(FileNameParam param) {
        this.folder = param.getFolder();
        this.newName = param.getNewName();
        this.prefix = param.getPrefix();
        this.suffix = param.getSuffix();
    }

    /**
     * @param file the file to upload
     * @return this
     */
    public FileNameBuilder file(MultipartFile file) {
        this.file = file;
        return this;
    }

    /**
     * @param newName the new name of the file
     * @return this
     */
    public FileNameBuilder newName(String newName) {
        this.newName = newName;
        return this;
    }

    /**
     * Sets the prefix for the file name.
     *
     * @param prefix the prefix to be added to the file name
     * @return this builder instance
     */
    public FileNameBuilder prefix(String prefix) {
        this.prefix = prefix + "_";
        return this;
    }


    /**
     * Sets the suffix for the file name.
     *
     * @param suffix the suffix to be added to the file name
     * @return this builder instance
     */
    public FileNameBuilder suffix(String suffix) {
        this.suffix = "_" + suffix;
        return this;
    }


    /**
     * Sets the new file name.
     *
     * @param fileName the new name of the file
     * @return this builder instance
     */
    public FileNameBuilder fileName(String fileName) {
        this.newName = fileName;
        return this;
    }

    /**
     * Sets the folder path where the file will be stored.
     *
     * @param folder the folder path
     * @return this builder instance
     */
    public FileNameBuilder folder(String folder) {
        this.folder = folder;
        return this;
    }

    public String getExtension() {
        return Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
    }

    /**
     * Builds the new file name based on the provided parameters.
     *
     * @return the new file name
     */
    public String build() {

        if (file == null) {
            throw new IllegalArgumentException("File must be provided");
        }

        logger.info("FileNameBuilder parameters: newName = {}, folder = {}, prefix = {}, suffix = {}", newName, folder, prefix, suffix);

        // Ensure folder always ends with a separator
        folder = folder != null ? folder.trim() + (folder.endsWith("/") ? "" : "/") : "";

        String originalFileName = file.getOriginalFilename();
        String originalFileNameWithoutExtension = originalFileName != null ? originalFileName: UUID.randomUUID().toString();

        // Build filename based on provided parameters
        StringBuilder sb = new StringBuilder(folder);
        sb.append(prefix != null ? prefix : "");
        sb.append(newName != null ? newName : originalFileNameWithoutExtension);
        sb.append(suffix != null ? suffix : "");
        sb.append(getExtension());

        logger.info("FileNameBuilder name generated: {}", sb.toString());
        return sb.toString();
    }
    
    public String buildx() {
        if (file == null) {
            throw new IllegalArgumentException("File must be provided");
        }

        logger.info("FileNameBuilder param newName :{}, folder :{}, prefix :{}, suffix :{}, folder : {}", newName, folder, prefix, suffix, folder);

        if (folder == null) {
            folder = "";
        } else {
            folder += "/";
        }
        String originalFileName = file.getOriginalFilename();

        if (prefix == null && suffix == null) {
            logger.info("prefix and suffix are null");
            String originalFileNameWithoutExtension = originalFileName != null ? originalFileName : UUID.randomUUID().toString();
            return folder + originalFileNameWithoutExtension + getExtension();
        }

        if (prefix == null) {
            prefix = "";
        }

        if (suffix == null) {
            suffix = "";
        }


        if (newName != null) {
            return folder + prefix + newName + suffix + getExtension();
        }

        String name = folder + prefix + "_" + UUID.randomUUID().toString() + "_" + suffix + getExtension();
        logger.info("FileNameBuilder name generated :{}", name);
        return name;
    }

    public FileNameParam buiFileNameParam() {
        FileNameParam fileNameParam = new FileNameParam();
        fileNameParam.setFolder(folder);
        fileNameParam.setNewName(newName);
        fileNameParam.setPrefix(prefix);
        fileNameParam.setSuffix(suffix);
        return fileNameParam;
    }
}
