/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 12:11:16
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 12:11:16
 */
package ca.deltagis.success.v1.adapters.web.http.logs;

import ca.deltagis.success.v1.adapters.web.api.endpoints.ApiEndpoints;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.application.services.logs.LogServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.logs.LogEntity;
import ca.deltagis.success.v1.domain.ports.in.ICommonService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.LOGS)
@Tag(name = "Logs API", description = "Operations related to the Log")
public class LogController extends AbstractCommonController<LogEntity> {
    @Autowired
    private LogServiceImpl service;

    @Getter
    private Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private ApiResponseService response;

    public Class<?> className() {
        return this.getClass();
    }

    public LogController(ApiResponseService apiResponseService, UserServiceImpl userServiceImpl) {
        // super(apiResponseService, userServiceImpl);
    }

    @Override
    public ICommonService<LogEntity> getService() {
        return service;
    }

    @Override
    public List<LogEntity> getDefault() {
        return getService().findAll();
    }
}
