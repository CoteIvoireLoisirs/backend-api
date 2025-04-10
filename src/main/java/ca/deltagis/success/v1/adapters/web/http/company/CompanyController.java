package ca.deltagis.success.v1.adapters.web.http.company;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.builder.ApiBuilder;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.adapters.web.api.builder.ErrorDetailBuilder;
import ca.deltagis.success.v1.adapters.web.http.company.request.CompanySaveResponse;
import ca.deltagis.success.v1.adapters.web.http.project.ProjectController;
import ca.deltagis.success.v1.adapters.web.message.CompanyMessage;
import ca.deltagis.success.v1.adapters.web.message.files.FileUploadError;
import ca.deltagis.success.v1.application.services.company.CompanyServiceImpl;
import ca.deltagis.success.v1.application.services.files.FileStorageServiceImpl;
import ca.deltagis.success.v1.application.services.logs.LogServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.company.Company;
import ca.deltagis.success.v1.domain.core.models.FileUploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;

@RestController
@RequestMapping(ApiEndpoints.COMPANY)
@Tag(name = "Company API", description = "Operations related to the Right")
// extends AbstractCommonController<Company>
public class CompanyController {

    @Getter
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private CompanyServiceImpl service;

    @Autowired
    private LogServiceImpl<Company> logService;

    @Autowired
    private ApiResponseService response;

     @Autowired
    FileStorageServiceImpl storageService;


    

    /**
     * Gets a company by its workspace code.
     *
     * @param workspaceCode The workspace code of the company to be retrieved.
     * @return The company with the specified workspace code.
     * @throws EntityNotFoundException if a company with the specified workspace
     *                                 code is not found.
     * @throws Exception               if an error occurs while retrieving the
     *                                 company.
     */
    @Operation(summary = "Get exercice by ID", description = "Fetches a exercice using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercice retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "exercice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    // @Loggable(action = AuditLogActionCode.AUTH_LOGIN_SUCCESS, message =
    // "Information exercice", actionFailed = AuditLogActionCode.AUTH_LOGIN_FAILED)
    @GetMapping("/{workspaceCode}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Company>> getByCode(
            @PathVariable String workspaceCode) {
        try {
            CompanySaveResponse get = service.findCompanyByWorkspaceCode(workspaceCode);
            if (get.getError() != null) {
                return response.error("Failed to retrieve entity", get.getError().name(), get.getErrorDetail(),
                        HttpStatus.BAD_REQUEST);
            }

            return response.success("company created successfully", get.getCompany(), HttpStatus.OK);
        } catch (Exception e) {
            HashMap<String, String> errors = new ErrorDetailBuilder().add("message", e.getMessage()).build();
            return response.error("Failed to create entity", e.getMessage(), errors, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get_company/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Company>> getById(
            @Parameter(description = "ID of the company", required = true) @PathVariable String id) {
        try {
            Company company = service.getById(Long.valueOf(id));
            if (company == null) {
                return response.error("Company not found", "No Company with the specified ID found", null, HttpStatus.NOT_FOUND);
            }
            company.setImageUrl(storageService.getUrlFile(company.getImagePath()));
            return response.success("Company retrieved successfully",company, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching entity with ID: {}", id, e);
         
            return response.error("Internal Server Error", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{workspace_code}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Company>> update(
            @RequestBody Company company) {
        try {
            CompanySaveResponse entity = service.updateCompany(company);
            if (entity.getClass() != null) {
                company.setImageUrl(storageService.getUrlFile(company.getImagePath()));
                return response.success("Comapgny updated successfully", entity.getCompany(), HttpStatus.OK);

            }
            return response.error("Failed to update compagny", entity.getError().name(), null,
                    HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return response.internalError("Failed to update company", e);
        }
    }

    @PostMapping(ApiEndpoints.COMPANY_CHANGE_IMAGE)
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<FileUploadResponse>> uploadImage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody() MultipartFile file,
            @PathVariable String code) {
        try {
            CompanySaveResponse entity = service.updateCompanyImage(file, code);
            FileUploadError errorType = entity.getError() != null
                    ? FileUploadError.fromFileUploadError(entity.getError())
                    : null;
            String message = (errorType == null || entity.getError() == CompanyMessage.NONE)
                    ? "Image uploaded successfully"
                    : entity.getError().name();

            return new ApiBuilder<FileUploadResponse>()
                    .withMessage(message)
                    .withData(new FileUploadResponse(file.getOriginalFilename(), errorType, message))
                    .withSuccess(errorType == null)
                    .withStatus(errorType == null ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                    .buildWithResponseEntity();
        } catch (RuntimeException e) {
            FileUploadResponse errorResponse = new FileUploadResponse(file.getOriginalFilename(),
                    FileUploadError.INVALID_FILE_TYPE, e.getMessage());
            return new ApiBuilder<FileUploadResponse>()
                    .withMessage(e.getMessage())
                    .withData(errorResponse)
                    .withSuccess(false)
                    .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .buildWithResponseEntity();
        }
    }

}
