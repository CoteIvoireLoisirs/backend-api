package ca.deltagis.success.v1.adapters.web.http.project;

import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.ApiResponse;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.application.services.project.UserProjectFavoriteImpl;
import ca.deltagis.success.v1.domain.core.entities.project.UserProjectFavorite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoints.PROJECT_FAVORITE)
@Tag(name = "Project Favorite API", description = "Operations related to project favorite")
public class UserProjectFavoriteController {
    @Autowired
    private UserProjectFavoriteImpl service;

    @Autowired
    private ApiResponseService response;

    Logger logger = LoggerFactory.getLogger(UserProjectFavoriteController.class);

    @Operation(summary = "Create Entity", description = "Creates a new entity.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to create entity"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<UserProjectFavorite>> toggle(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody UserProjectFavorite favorite
    ) {
        try {
            UserProjectFavorite entity = service.saveOrDelete(favorite);
            return response.success(entity != null ? "Project added to favorites" : "Project removed from favorites", entity, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error creating entity", e);
            return response.internalError("Failed to create entity", e);
        }
    }
}
