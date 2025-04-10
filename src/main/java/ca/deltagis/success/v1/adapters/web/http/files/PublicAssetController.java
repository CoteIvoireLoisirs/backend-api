package ca.deltagis.success.v1.adapters.web.http.files;

import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.application.services.files.FileStorageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoints.PUBLIC_ASSET_ENDPOINT)
@Tag(name = "Public Asset API", description = "Operations related to public assets")
public class PublicAssetController {
    String ASSET_PATH = "/assets/";

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    Logger logger = LoggerFactory.getLogger(PublicAssetController.class);

    @Autowired
    private HttpServletRequest servletRequest;

    @Operation(summary = "Retrieves a public asset based on the URL path.", description = "Returns the asset that matches the URL path.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset found successfully"),
            @ApiResponse(responseCode = "404", description = "Asset not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/**")
    public ResponseEntity<byte[]> getFile() {
        String fileName = extractFileNameFromRequest();
        if (fileName == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Resource resource = fileStorageService.loadFileAsResource(fileName);
            if (resource == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            byte[] bytes = resource.getInputStream().readAllBytes();
            HttpHeaders headers = createHeaders(resource.getFilename(), fileName);
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving asset: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a public asset based on the URL path.
     *
     * @return ResponseEntity containing the asset or an error message.
     */
    private String extractFileNameFromRequest() {
        logger.info("Current URL: {}", servletRequest.getRequestURI());
        String path = servletRequest.getRequestURI().replace(ASSET_PATH, "");
        logger.info("Extracted path: {}", path);
        return path.isEmpty() ? null : path;
    }

    /**
     * Returns the asset that matches the URL path.
     *
     * @return ResponseEntity containing the asset or an error message.
     */
    private HttpHeaders createHeaders(String filename, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = fileStorageService.determineMediaType(fileName);
        logger.info("Media type: {}", mediaType);

        headers.setContentType(mediaType);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");

        return headers;
    }
}



