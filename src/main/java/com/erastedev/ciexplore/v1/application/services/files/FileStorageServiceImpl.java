package com.erastedev.ciexplore.v1.application.services.files;

import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;
import com.erastedev.ciexplore.v1.application.utils.ServerUtil;
import com.erastedev.ciexplore.v1.application.validator.out.uploader.FileValidator;
import com.erastedev.ciexplore.v1.application.validator.out.uploader.ImageFileValidator;
import com.erastedev.ciexplore.v1.domain.models.FileNameParam;
import com.erastedev.ciexplore.v1.domain.models.FileOperationResponse;
import com.erastedev.ciexplore.v1.domain.models.FileUploadResponse;
import com.erastedev.ciexplore.v1.domain.ports.in.uploader.IFileStorageService;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Implementation of the IFileStorageService interface for managing file storage
 * operations.
 */
@Service
public class FileStorageServiceImpl implements IFileStorageService {
    private final Path fileStorageLocation;

    private final FileValidator fileValidator;

    private final ImageFileValidator imageFileValidator;

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final ServerUtil serverUtil;

    private final FilePathManager pathManager;

    public String PUBLIC_ASSET_PATH = "/assets/";

    @Autowired
    public FileStorageServiceImpl(
            Path fileStorageLocation,
            FileValidator fileValidator,
            ImageFileValidator imageFileValidator,
            ServerUtil serverUtil,
            FilePathManager pathManager
    ) {
        this.fileStorageLocation = fileStorageLocation;
        this.fileValidator = fileValidator;
        this.imageFileValidator = imageFileValidator;
        this.serverUtil = serverUtil;
        this.pathManager = pathManager;
        if (serverUtil != null) {
            PUBLIC_ASSET_PATH = serverUtil.getAssetFolder();
        }
    }

    @Override
    public boolean createFolder(String folder) {
        try {
            Files.createDirectories(fileStorageLocation.resolve(folder));
            return true;
        } catch (IOException e) {
            logger.error("Failed to create folder  {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createfolderfromfilenameparam(FileNameParam fileNameParam) {
        return createFolder(fileNameParam.getFolder());
    }

    /**
     * Constructs a FileNameBuilder using the provided file and optional parameters.
     *
     * @param file  the file to be used in building the file name
     * @param param optional parameters for customizing the file name
     * @return a FileNameBuilder instance configured with the specified file and
     * parameters
     */
    private FileNameBuilder getFileNameBuilder(MultipartFile file, FileNameParam param) {
        FileNameBuilder fileNameBuilder;
        if (param == null) {
            fileNameBuilder = new FileNameBuilder()
                    .file(file)
                    .fileName(file.getOriginalFilename());
        } else {
            fileNameBuilder = new FileNameBuilder(param).file(file);
        }
        logger.info("FileNameBuilder param newName :{}, folder :{}, prefix :{}, suffix :{}, folder : {}",
                param.getNewName(), param.getFolder(), param.getPrefix(), param.getSuffix(), param.getFolder());

        return fileNameBuilder;
    }

    /**
     * Validates an image file and returns its original filename.
     *
     * @param file the image file to validate
     * @return the original filename if validation succeeds, otherwise null
     */
    @Override
    public FileUploadResponse imageValidation(MultipartFile file) {
        try {
            FileUploadError imageUploadError = imageFileValidator.validate(file);
            if (imageUploadError != null) {
                return new FileUploadResponse(file.getOriginalFilename(), imageUploadError);
            }

            return new FileUploadResponse(file.getOriginalFilename(), getUrlFile(file.getOriginalFilename()),
                    file.getContentType(), file.getSize());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Image validation failed: {}", e.getMessage());
            return new FileUploadResponse(file.getOriginalFilename(), FileUploadError.INVALID_FILE_TYPE);
        }
    }

    /**
     * Stores a file in the designated storage location.
     *
     * @param file  the file to store
     * @param param optional parameters for naming the file
     * @return the name of the stored file or null if the filename is invalid
     */
    @Override
    public FileUploadResponse storeFile(MultipartFile file, @Null FileNameParam param) {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            return new FileUploadResponse(null, FileUploadError.EMPTY_FILE);
        }

        try {
            if (param != null && param.getFolder() != null) {
                createfolderfromfilenameparam(param);
            }
            FileNameBuilder fileNameBuilder = getFileNameBuilder(file, param);
            String name = fileNameBuilder.build();

            Path targetLocation = this.fileStorageLocation.resolve(name);
            logger.info("debug targetLocation: {}", targetLocation);
            long copyState = Files.copy(
                    fileNameBuilder.getFile().getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            if (copyState == 0L) {
                return new FileUploadResponse(name, FileUploadError.FAILED_TO_SAVE_FILE);
            }

            // Return the new name of the stored file
            return new FileUploadResponse(name, getUrlFile(name), file.getContentType(), file.getSize());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not store file {}: {}", fileName, e.getMessage());
            return new FileUploadResponse(fileName, FileUploadError.INVALID_FILE, e.getMessage());
        }
    }

    @Transactional
    public FileOperationResponse deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return new FileOperationResponse(false, "File name is empty or null");
        }

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            if (!targetLocation.startsWith(targetLocation)) {
                return new FileOperationResponse(false, "Invalid file path: potential security risk");
            }
            if (Files.exists(targetLocation)) {

                try (FileChannel channel = FileChannel.open(targetLocation, StandardOpenOption.WRITE)) {
                    channel.lock();
                    System.gc();
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("Impossible de verrouiller le fichier : " + e.getMessage());
                }
                Files.delete(targetLocation);
                System.out.println("Fichier supprimé avec succès : " + targetLocation);
                return new FileOperationResponse(true, "File successfully deleted: " + fileName);
            } else {
                System.out.println("Fichier non trouvé : " + targetLocation);
                return new FileOperationResponse(false, "File not found: " + fileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Could not delete file {}: {}", fileName, e.getMessage());
            return new FileOperationResponse(false, "Error deleting file: " + e.getMessage());
        }
    }

    /**
     * Stores an image after validating it.
     *
     * @param file  the image to store
     * @param param parameters for naming the image
     * @return the name of the stored image or null in case of error
     */
    @Override
    public FileUploadResponse storeImage(MultipartFile file, FileNameParam param) {
        FileUploadResponse validator = imageValidation(file);

        // Validation failed
        if (validator.getError() != null) {
            return new FileUploadResponse(validator.getFileName(), validator.getError());
        }

        return storeFile(file, param);
    }

    /**
     * Loads a file as a Resource.
     *
     * @param fileName the name of the file to load
     * @return the Resource representing the file or null if it does not exist
     */
    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            logger.info("loadFileAsResource>fileName: {}", fileName);
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                // Return resource if it exists
                return resource;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error loading resource {}: {}", fileName, e.getMessage());
        }
        // Resource not found
        return null;
    }

    /**
     * Checks if a specified path exists in the storage location.
     *
     * @param path the path to check
     * @return true if the path exists, otherwise false
     */
    @Override
    public boolean fileExists(String path) {
        try {
            return Files.exists(Path.of(path));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the full URL of a file based on its relative path.
     * <p>
     * If the path is already an absolute URL (starting with "http://" or "https://"),
     * it returns the path as-is. Otherwise, it constructs a URL using the base server
     * URL and the public asset path, and checks if the file exists at the resolved
     * location. If the file exists, it returns the constructed URL; otherwise, it
     * returns null.
     *
     * @param relativePath the relative path of the file
     * @return the full URL of the file if it exists, or null if it does not exist
     */
    @Override
    public String getUrlFile(String relativePath) {
        if (relativePath == null) {
            return null;
        } else if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath;
        }

        String baseUrl = serverUtil.url().replace(".", "");
        String path = pathManager.asset(relativePath);
        Path filePath = fileStorageLocation.resolve(relativePath);

        boolean fileExist = Files.exists(filePath);

        logger.info("GetFile path: {}, baseUrl: {}, fileExist: {}", filePath, baseUrl, fileExist);

        // Return full URL if exists
        if (fileExist) {
            logger.info("File exists : {}", baseUrl + path);
            return (baseUrl + path)
                    .replace("./", "/")
                    .replace("opt/tomcat-ressources/success-web-api/", "")
                    .replace("assets//", "assets/")
                    .replace("successwebdelta-monitoringcom", "success.web.delta-monitoring.com/success_web_api");
        }

        return null;
    }

    /**
     * Retrieves the bytes of a specified file.
     *
     * @param fileName the name of the file to retrieve
     * @return byte array of the file or null in case of error
     */
    @Override
    public byte[] retrieveFile(String fileName) {
        logger.info("Retrieving asset: {}", fileName);
        Resource resource = loadFileAsResource(fileName);

        if (resource == null || !resource.exists()) {
            logger.error("Asset not found: {}", fileName);
            // Asset not found
            return null;
        }

        try {
            // Return bytes of the resource
            return resource.getInputStream().readAllBytes();
        } catch (IOException e) {
            logger.error("Error retrieving asset: {}", e.getMessage());
            // Error during retrieval
            return null;
        }
    }

    /**
     * Determines the MIME type of a given filename.
     *
     * @param fileName the name of the file
     * @return MediaType corresponding to the filename extension or
     * application/octet-stream for unknown types
     */
    @Override
    public MediaType determineMediaType(String fileName) {
        if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (fileName.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF;
        } else if (fileName.endsWith(".json")) {
            return MediaType.APPLICATION_JSON;
        }

        // Default for unknown types
        return MediaType.APPLICATION_OCTET_STREAM;
    }

    /**
     * Reads a JSON file and returns a collection of objects.
     *
     * @param fileName the name of the JSON file to read
     * @return a list of objects read from the JSON file or null in case of error
     */
    @Override
    public List<Object> readJsonFileCollection(String fileName) {
        try {
            boolean checkFile = fileExists(fileName);
            logger.info("Check Countries file exist: {}", checkFile);
            if (checkFile) {
                File countriesFile = new File(fileName);
                Path path = countriesFile.toPath();
                byte[] jsonData = Files.readAllBytes(path);

                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class);

                return objectMapper.readValue(jsonData, collectionType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
