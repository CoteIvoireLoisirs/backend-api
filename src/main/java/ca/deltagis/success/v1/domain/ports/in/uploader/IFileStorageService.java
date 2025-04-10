
package ca.deltagis.success.v1.domain.ports.in.uploader;

import ca.deltagis.success.v1.domain.core.models.FileNameParam;
import ca.deltagis.success.v1.domain.core.models.FileUploadResponse;
import jakarta.validation.constraints.Null;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-03 20:18:12
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/ports/in/uploader/IFileStorageService.java
 * @Description: Interface for the file storage service.
 */
public interface IFileStorageService {
    /**
     * Creates a folder in the storage location.
     *
     * @param folder the name of the folder to be created
     */
    boolean createFolder(String folder);

    /**
     * Checks if a specified path exists in the storage location.
     *
     * @param fileNameParam the path to check
     * @return true if the path exists, otherwise false
     */
    boolean createfolderfromfilenameparam(FileNameParam fileNameParam);

    /**
     * Validates an image and returns its name.
     *
     * @param file the file to validate
     * @return the name of the file if validation succeeds, otherwise null
     */
    FileUploadResponse imageValidation(MultipartFile file);

    /**
     * Stores a file and returns its name.
     *
     * @param file  the file to store
     * @param param optional parameters for the file name
     * @return the name of the stored file or null if the file name is invalid
     */
    FileUploadResponse storeFile(MultipartFile file, @Null FileNameParam param);

    /**
     * Stores an image after validation and returns its name.
     *
     * @param file  the image file to store
     * @param param parameters for the file name
     * @return the name of the stored file or null in case of error
     */
    FileUploadResponse storeImage(MultipartFile file, FileNameParam param);

    /**
     * Loads a file as a resource.
     *
     * @param fileName the name of the file to load
     * @return the resource associated with the file or null if the file does not exist
     */
    Resource loadFileAsResource(String fileName);

    /**
     * Checks if a file exists at a given path.
     *
     * @param path the path of the file
     * @return true if the file exists, otherwise false
     */
    boolean fileExists(String path);

    /**
     * Retrieves the URL of a file from a given path.
     *
     * @param path the path of the file
     * @return the URL of the file or null if the file does not exist
     */
    String getUrlFile(String path);

    /**
     * Retrieves the bytes of a file by its name.
     *
     * @param fileName the name of the file to retrieve
     * @return bytes of the file or null in case of error
     */
    byte[] retrieveFile(String fileName);

    /**
     * Determines the MIME file type based on its name.
     *
     * @param fileName the name of the file
     * @return the MIME type corresponding to the file
     */
    MediaType determineMediaType(String fileName);


    /**
     * Reads a JSON file and returns a collection of objects.
     *
     * @param fileName the name of the JSON file to read
     * @return a list of objects read from the JSON file or null in case of error
     */
    List<Object> readJsonFileCollection(String fileName);
}
