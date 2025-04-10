package com.erastedev.ciexplore.v1.infrastructure.task.files;

import com.erastedev.ciexplore.v1.application.utils.ServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-04 06:07:15
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/application/services/files/DirectoryCreator.java
 * @Description: Creates a directory if it doesn't exist
 */
@Component
public class DirectoryCreator implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(DirectoryCreator.class);

    private String DIRECTORY_PATH;

    private final String WORKSPACES_DIRECTORY_PATH = "workspaces/";

    @Autowired
    public DirectoryCreator(ServerUtil serverUtil) {
        if (serverUtil.getAssetFolder() != null) {
            this.DIRECTORY_PATH = serverUtil.getAssetFolder();
        }
    }


    /**
     * The default directories used by the application.
     */
    private String[] defaultDirectories() {
        try {
            String base = this.DIRECTORY_PATH == null ? "/asset/" : this.DIRECTORY_PATH;
            return new String[]{
                    DIRECTORY_PATH,
                    base + WORKSPACES_DIRECTORY_PATH
            };
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates the default directories used by the application.
     * <p>
     */
    @Override
    public void run(String... args) {
        try {
            Arrays.stream(Objects.requireNonNull(defaultDirectories())).forEach((directoryPath) -> {
                if (directoryPath == null) {
                    logger.error("Directory path is null");
                    return;
                }

                File directory = new File(directoryPath);

                if (!directory.exists()) {
                    boolean created = directory.mkdirs();
                    if (created) {
                        logger.info("Successfully created directory {} ", directoryPath);
                    } else {
                        logger.error("Failed to create directory {} ", directoryPath);
                    }
                } else {
                    logger.info("Directory {} already exists", directoryPath);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
