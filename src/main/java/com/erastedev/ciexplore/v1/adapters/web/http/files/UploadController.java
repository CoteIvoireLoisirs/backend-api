package com.erastedev.ciexplore.v1.adapters.web.http.files;

import com.erastedev.ciexplore.v1.adapters.web.api.endpoints.ApiEndpoints;
import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;
import com.erastedev.ciexplore.v1.application.services.files.FileNameBuilder;
import com.erastedev.ciexplore.v1.application.services.files.FileStorageServiceImpl;
import com.erastedev.ciexplore.v1.application.validator.out.uploader.ImageFileValidator;
import com.erastedev.ciexplore.v1.domain.models.FileNameParam;
import com.erastedev.ciexplore.v1.domain.models.FileUploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiEndpoints.UPLOAD)
@Tag(name = "Upload API", description = "Operations related to file upload")
public class UploadController {

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @Autowired
    private ImageFileValidator imageFileValidator;

    @Operation(summary = "Upload Image", description = "Uploads an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<FileUploadResponse> uploadImage(@RequestBody() MultipartFile file) throws Exception {
        try {
            FileNameParam param =  new FileNameBuilder().file(file).folder("workspaces").newName(String.valueOf(12)).prefix("img").suffix("jpg").buiFileNameParam();
            FileUploadResponse fileUpload = fileStorageService.storeImage(file, param);
            if (fileUpload.getError() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fileUpload);
            }
            return ResponseEntity.ok(fileUpload);
        } catch (RuntimeException e) {
            e.printStackTrace();
            FileUploadResponse errorResponse = new FileUploadResponse(file.getOriginalFilename(), FileUploadError.INVALID_FILE_TYPE, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
