/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 10:59:34
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 10:59:34
 */
package ca.deltagis.success.v1.adapters.web.http.rights.profile;

import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.application.request.right.CreateModuleProfileRightRequest;
import ca.deltagis.success.v1.application.request.right.CreateModuleProfileRightResponse;
import ca.deltagis.success.v1.adapters.web.message.rights.CreateModuleProfileRightError;
import ca.deltagis.success.v1.application.services.rights.ModuleProfileRightServiceImpl;
import ca.deltagis.success.v1.application.services.rights.profile.ProfileServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.rights.ModuleProfileRight;
import ca.deltagis.success.v1.domain.core.entities.rights.profil.Profile;
import ca.deltagis.success.v1.domain.ports.in.ICommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.PROFILES)
@Tag(name = "Profile API", description = "Operations related to the profile")
//  extends AbstractCommonController<Profile>
public class ProfileController {
    @Autowired
    private ProfileServiceImpl service;

    @Autowired
    private ModuleProfileRightServiceImpl moduleProfileRightService;

    @Getter
    private Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ApiResponseService response;

    public Class<?> className() {
        return this.getClass();
    }

    // @Override
    public ICommonService<Profile> getService() {
        return service;
    }

    public List<Profile> getDefault() {
        return getService().findAll();
    }

    /**
     * Retrieves all entities.
     *
     * @param allParams Optional query parameters for filtering entities.
     * @return ResponseEntity containing the list of entities or an error message.
     */
    // @Override
   /* @Operation(summary = "Get All Entities", description = "Fetches all entities, optionally filtered by query parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entities retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<List<ModuleProfileRight>>> getAllModuleRight(
            @Parameter(description = "Optional query parameters for filtering", required = false)
            @Nullable @RequestParam Map<String, String> allParams) {
        try {
            List<ModuleProfileRight> entities = moduleProfileRightService.getAll();
            return response.success("Entities retrieved successfully", entities, HttpStatus.OK);
        } catch (Exception e) {
            return response.internalError("Internal Server Error", e);
        }
    }*/

    /**
     * Retrieves a list of all ModuleProfileRight objects.
     *
     * <p>This method retrieves a list of ModuleProfileRight objects and returns them
     * in a successful ApiResponse. In case of an exception, an error response is returned.
     *
     * @return ResponseEntity containing the list of ModuleProfileRight objects
     * or an error message.
     */
    @Operation(summary = "get single profile by id", description = "get single profile by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get single profile by id"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<ModuleProfileRight>> getSingle(
            @PathVariable int id
    ) {
        try {
            ModuleProfileRight data = moduleProfileRightService.getModuleProfileRight((long) id);
            if (data != null) {
                return response.success("Module Profile Right", data, HttpStatus.OK);
            }
            return response.error("Not found", "Not found", null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return response.internalError("Failed to retrieve module profile rights", e);
        }
    }

    /**
     * Creates a new module profile with authorization.
     *
     * <p>This method retrieves a list of ModuleProfileRight objects and returns them
     * in a successful ApiResponse. In case of an exception, an error response is returned.
     *
     * @return ResponseEntity containing the list of ModuleProfileRight objects
     * or an error message.
     */
    @Operation(summary = "List profile by right and module", description = "List profile by right and module.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List profile by right and module"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<List<ModuleProfileRight>>> getAll() {
        try {
            List<ModuleProfileRight> data = moduleProfileRightService.getModuleProfileRightList();
            return response.success("Module Profile Right", data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error creating entity", e);
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            return response.error("Failed to retrieve module profile rights", e.getMessage(), errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a list of all ModuleProfileRight objects.
     *
     * <p>This method retrieves a list of ModuleProfileRight objects and returns them
     * in a successful ApiResponse. In case of an exception, an error response is returned.
     *
     * @return ResponseEntity containing the list of ModuleProfileRight objects
     * or an error message.
     */
    @Operation(summary = "Create Profile with rights", description = "Creates a profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<ModuleProfileRight>> create(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody CreateModuleProfileRightRequest param) {
        try {
            CreateModuleProfileRightResponse entity = moduleProfileRightService.createProfileWithRight(param);
            if (entity.getError() == null || entity.getError() == CreateModuleProfileRightError.NONE) {
                return response.success("Profile has been created successfully", entity.getModuleProfileRight(), HttpStatus.CREATED);
            }
            return response.error(entity.getMessage(), entity.getError().name(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return response.internalError("Failed to create profile and rights", e);
        }
    }

    /**
     * Updates a ModuleProfileRight entity.
     *
     * <p>This method updates a ModuleProfileRight entity and returns them
     * in a successful ApiResponse. In case of an exception, an error response is returned.
     *
     * @param id Id of the ModuleProfileRight to be updated.
     * @return ResponseEntity containing the updated ModuleProfileRight object
     * or an error message.
     */
    @Operation(summary = "Update Profile with rights", description = "Updates a profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<ModuleProfileRight>> update(
            @Parameter(description = "Id of the entity to be updated", required = true) @PathVariable Long id,
            @Parameter(description = "Entity data to be updated", required = true) @RequestBody CreateModuleProfileRightRequest param
    ) {
        try {
            CreateModuleProfileRightResponse entity = moduleProfileRightService.updateProfileWithRight(id, param);
            if (entity.getError() == null || entity.getError() == CreateModuleProfileRightError.NONE) {
                return response.success("Profile has been updated successfully", entity.getModuleProfileRight(), HttpStatus.OK);
            }
            return response.error(entity.getMessage(), entity.getError().name(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to update profile and rights", e);
        }
    }

    /**
     * Deletes a ModuleProfileRight entity.
     *
     * <p>This method deletes a ModuleProfileRight entity and returns a successful ApiResponse.
     * In case of an exception, an error response is returned.
     *
     * @param id Id of the ModuleProfileRight to be deleted.
     * @return ResponseEntity containing the result of the deletion operation
     * or an error message.
     */
    @Operation(summary = "Update Profile with rights", description = "Updates a profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<ModuleProfileRight>> update(
            @Parameter(description = "Id of module profile right to be updated", required = true) @PathVariable Long id
    ) {
        try {
            CreateModuleProfileRightResponse entity = moduleProfileRightService.deleteProfileWithRight(id);
            if (entity.getError() == null || entity.getError() == CreateModuleProfileRightError.NONE) {
                return response.success("Profile has been deleted successfully", entity.getModuleProfileRight(), HttpStatus.OK);
            }

            return response.error(entity.getMessage(), entity.getError().name(), null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error updating entity", e);
            return response.error("Failed to delete profile and rights", e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
