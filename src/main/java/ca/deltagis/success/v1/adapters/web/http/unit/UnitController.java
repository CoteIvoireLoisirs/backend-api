package ca.deltagis.success.v1.adapters.web.http.unit;

import java.util.HashMap;
import java.util.List;

import ca.deltagis.success.v1.domain.ports.out.AbstractCommonController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.deltagis.success.v1.adapters.web.api.builder.ErrorDetailBuilder;
import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.adapters.web.message.UnitedMessage;
import ca.deltagis.success.v1.application.services.unit.UnitServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.unit.Unit;
import ca.deltagis.success.v1.domain.ports.in.ICommonService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;

@RestController
@RequestMapping(ApiEndpoints.UNIT)
@Tag(name = "Unit API", description = "Operations related to the Unit entity")
public class UnitController extends AbstractCommonController<Unit> {

    @Getter
    private Logger logger = LoggerFactory.getLogger(UnitController.class);

    @Autowired
    private ApiResponseService response;

    @Autowired
    private UnitServiceImpl service;

    public Class<?> className() {
        return this.getClass();
    }

    public ICommonService<Unit> getService() {
        return service;
    }

    @Override
    public List<Unit> getDefault() {
        return service.getAll();
    }

    @Override
    @PostMapping
    public ResponseEntity<ca.deltagis.success.v1.adapters.web.api.ApiResponse<Unit>> create(
            @Parameter(description = "Entity data to be created", required = true) @RequestBody Unit united) {
        try {
            UnitedSaveResponse entity = service.createNewUnit(united);

            if (entity.getError() == UnitedMessage.CREATED) {
                return response.success("Entity created successfully", entity.getUnited(), HttpStatus.CREATED);

            }
            if (entity.getError() == UnitedMessage.ALREADY_EXISTS) {
              
                return response.error(entity.getError().name(), entity.getError().name(), null, HttpStatus.BAD_REQUEST);
            }

            return response.error("Failed to create united", entity.getError().name(), null,
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e) {

            HashMap<String, String> errors = new ErrorDetailBuilder().add("message", e.getMessage()).build();
            return response.error("Failed to create entity", e.getMessage(), errors, HttpStatus.BAD_REQUEST);
        }
    }
}
