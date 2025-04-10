package ca.deltagis.success.v1.application.services.files;

import ca.deltagis.success.v1.application.utils.ServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FilePathManager {
    Logger logger = LoggerFactory.getLogger(FilePathManager.class);

    @Autowired
    ServerUtil serverUtil;

    /**
     * Determines the appropriate resource path based on the environment.
     *
     * <p>Returns the production path if the application is running in
     * production mode, otherwise returns the local resource path.
     *
     * @return the resource path as a String
     */
    private String resourcePath() {
        try {
            logger.info("resourcePath>serverUtil.getResourceFolder: {}", serverUtil.getResourceFolder());
            return serverUtil.getResourceFolder();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the asset path based on the environment.
     *
     * <p>If the serverUtil is not null and the asset folder is not null,
     * it will return the asset folder. Otherwise, it will return null.
     *
     * @return the asset path as a String
     */
    private String assetPath() {
        try {
            logger.info("resourcePath>serverUtil.getAssetFolder: {}", serverUtil.getAssetFolder());
            return serverUtil.getAssetFolder();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a full path from a resource file name.
     *
     * <p>Given a resource file name, this method will return the full path
     * to that file. The path is determined by the environment, falling back
     * to the local resource path if not in production.
     *
     * @param fileName the name of the resource file
     * @return the full path to the resource file
     */
    public String fromResource(String fileName) {
        try {
            if (fileName == null) {
                return null;
            }

            StringBuilder pathBuilder = new StringBuilder(Objects.requireNonNull(resourcePath()));
            if (!fileName.startsWith("/")) {
                pathBuilder.append('/');
            }
            pathBuilder.append(fileName);

            logger.info("fromResource>pathBuilder: {}", pathBuilder);
            return pathBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a full path from an asset file name.
     *
     * <p>Given an asset file name, this method will return the full path
     * to that file. The path is determined by the environment, falling back
     * to the local asset path if not in production.
     *
     * @param fileName the name of the asset file
     * @return the full path to the asset file
     */
    public String fromAsset(String fileName) {
        try {
            if (fileName == null) {
                return null;
            }

            StringBuilder pathBuilder = new StringBuilder(Objects.requireNonNull(assetPath()));
            if (!fileName.startsWith("/")) {
                pathBuilder.append('/');
            }
            pathBuilder.append(fileName);

            logger.info("fromAsset>pathBuilder: {}", pathBuilder);
            return pathBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a resource path to an asset path.
     *
     * <p>Given a resource file name, this method will return the asset path
     * to that file, which is the resource path with the "/resources" prefix
     * removed.
     *
     * @param fileName the name of the resource file
     * @return the asset path to the resource file
     */
    public String asset(String fileName) {
        try {
            String pathBuilder = fromAsset(fileName);
            return pathBuilder.replace("/resources", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
