package ca.deltagis.success.v1.infrastructure.config.FileConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {
    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @Bean
    public Path fileStoragePath() {
        return Paths.get(fileStorageLocation).toAbsolutePath().normalize();
    }
}