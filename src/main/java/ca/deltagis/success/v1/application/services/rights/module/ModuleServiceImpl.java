/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 12:43:09
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 12:43:09
 */
package ca.deltagis.success.v1.application.services.rights.module;

import ca.deltagis.success.v1.application.services.workspace.WorkspaceServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.rights.module.Module;
import ca.deltagis.success.v1.domain.core.models.rigths.ModuleEnum;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.rights.module.IModuleService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.rights.module.ModuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl extends AbstractCommonService<Module> implements IModuleService {
    @Autowired
    private ModuleRepository repository;

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Override
    public List<Module> findAll() {
        return repository.findAll();
    }

    @Override
    public Long deleteById(Long id) {
        repository.deleteById(id);
        return id;
    }

    @Override
    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public Module save(Module entity) {
        return repository.save(entity);
    }

    @Override
    public ICommonRepository<Module> getRepository() {
        return repository;
    }

    @Override
    public List<Module> getAll() {
        createDefaultModulesIfNotExist();
        return getRepository().findAllByDeletedIsNull();
    }

    /**
     * Retrieves a list of modules that are currently enabled.
     *
     * @return a list of enabled Module entities.
     */
    @Override
    public List<Module> getEnabledModules() {
        return repository.findByEnableIsTrue();
    }

    /**
     * Checks if any enabled modules exist in the repository.
     * If none exist, it creates and saves a list of default modules
     * based on the values from ModuleEnum, setting each module as enabled.
     */
    @Override
    public void createDefaultModulesIfNotExist() {
        logger.info("CREATE_DEFAULT_MODULES_IF_NOT_EXIST :: start createDefaultModulesIfNotExist ... count :: {}", repository.count());
        if (!repository.existsByEnableIsTrue()) {
            logger.info("CREATE_DEFAULT_MODULES_IF_NOT_EXIST :: create default modules");
            List<Module> defaultModules = ModuleEnum.getAll()
                    .stream()
                    .map(moduleEnum -> {
                        Module module = new Module();
                        module.setAutoFields();
                        module.setName(moduleEnum.getCode());
                        module.setEnable(true);
                        return module;
                    })
                    .collect(Collectors.toList());
            repository.saveAll(defaultModules);
        }
        logger.info("CREATE_DEFAULT_MODULES_IF_NOT_EXIST :: end createDefaultModulesIfNotExist ...");
    }
}
