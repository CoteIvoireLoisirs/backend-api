package ca.deltagis.success.v1.adapters.web.http.currency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.deltagis.success.v1.adapters.web.api.builder.ApiBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.deltagis.success.v1.adapters.web.api.ApiResponse;
import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;

import ca.deltagis.success.v1.adapters.web.http.project.ProjectController;
import ca.deltagis.success.v1.adapters.web.message.CurrencyMessage;
import ca.deltagis.success.v1.application.services.currency.CurrencyServiceImpl;
import ca.deltagis.success.v1.application.services.files.FileStorageServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.currency.Currency;
import ca.deltagis.success.v1.domain.ports.in.ICommonService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonController;
import ca.deltagis.success.v1.infrastructure.repository.currency.CurrencyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(ApiEndpoints.CURRENCY)
@Tag(name = "Currency API", description = "Operations related to the Right")
public class CurrencyController {

    @Autowired
    private FileStorageServiceImpl fileService;

    @Getter
    private Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private CurrencyRepository repository;

    @Autowired
    private CurrencyServiceImpl service;

    @Autowired
    private ApiResponseService response;

    public Class<?> className() {
        return this.getClass();
    }

    private String jsonPath = "resources/data/currencies.json";

    /**
     * Get all currencies.
     *
     * @return List of all currencies.
     */
    @Operation(summary = "List countries", description = "Get all countries")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Countries data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<Currency>>> getAll() {
        try {
            // List<Currency> json = service.readJsonFileCollection(jsonPath);
            return response.success("All Currencies", service.getAllCurrencies(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiBuilder().internalServerError("Server error occurred", e);
        }
    }
}
