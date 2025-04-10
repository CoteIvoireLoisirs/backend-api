package ca.deltagis.success.v1.adapters.web.http;

import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.ApiResponse;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.adapters.web.http.user.UserAuthController;
import ca.deltagis.success.v1.adapters.web.message.user.UserCustomMessage;
import ca.deltagis.success.v1.application.services.files.FileStorageServiceImpl;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.COUNTRIES)
@Tag(name = "Country API", description = "Operations related to country data")
public class CountryController {

    @Autowired
    private FileStorageServiceImpl fileService;

    public Logger logger = LoggerFactory.getLogger(UserAuthController.class);

    @Autowired
    public ApiResponseService response;

    private String jsonPath = "resources/data/countries.json";

    @Operation(summary = "List countries", description = "Get all countries")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Countries data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<Object>>> getCountries() {
        try {
            List<Object> json = fileService.readJsonFileCollection(jsonPath);
            return response.success("All Countries", json, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            HashMap<String, String> errorDetails = new HashMap<>();
            errorDetails.put("message", e.getMessage());
            return response.error("Error logging in user", UserCustomMessage.INTERNAL_ERROR.getMessage(), errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
