/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 12:37:29
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 12:37:29
 */
package ca.deltagis.success.v1.adapters.web.http.rights.module;

import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.application.services.logs.LogServiceImpl;
import ca.deltagis.success.v1.application.services.rights.module.ModuleServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.rights.module.Module;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(ApiEndpoints.MODULES)
@Tag(name = "Module API", description = "Operations related to the module")
public class ModuleController {
    //  extends AbstractCommonController<Module>

    @Autowired
    private ModuleServiceImpl service;

    @Autowired
    private ApiResponseService response;

    @Getter
    private Logger logger = LoggerFactory.getLogger(ModuleController.class);

    public Class<?> className() {
        return this.getClass();
    }

    public ModuleController(ApiResponseService apiResponseService, UserServiceImpl userServiceImpl, LogServiceImpl logService) {
        // super(apiResponseService, userServiceImpl, logService);userServiceImpl
    }

    /**
     * Retrieves a list of all modules.
     *
     * <p>This method retrieves a list of modules and returns them in a successful ApiResponse. In case of an exception, an error response is returned.
     *
     * @return ResponseEntity containing the list of modules or an error message.
     */
    @Operation(summary = "Get All Modules", description = "List all modules")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modules retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<List<Module>>> getAll() {
        try {
            return response.success("Entities retrieved successfully", service.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to retrieve entities", e);
        }
    }
}
