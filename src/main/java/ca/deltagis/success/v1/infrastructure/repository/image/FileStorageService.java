package ca.deltagis.success.v1.infrastructure.repository.image;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file); Path loadFile(String fileName);

}
