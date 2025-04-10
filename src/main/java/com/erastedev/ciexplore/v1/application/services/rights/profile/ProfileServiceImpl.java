/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 10:39:41
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 10:39:41
 */
package com.erastedev.ciexplore.v1.application.services.rights.profile;

import com.erastedev.ciexplore.v1.application.services.rights.ModuleProfileRightServiceImpl;
import com.erastedev.ciexplore.v1.application.services.rights.RightServiceImpl;
import com.erastedev.ciexplore.v1.application.services.rights.module.ModuleServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserAuthServiceImpl;
import com.erastedev.ciexplore.v1.application.services.workspace.WorkspaceServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.rights.profil.Profile;
import com.erastedev.ciexplore.v1.domain.models.rigths.DefaultSystemRight;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import com.erastedev.ciexplore.v1.domain.ports.in.rights.profile.IProfileService;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonService;
import com.erastedev.ciexplore.v1.infrastructure.repository.rights.profile.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl extends AbstractCommonService<Profile> implements IProfileService {
    @Autowired
    private ProfileRepository repository;

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @Autowired
    private ModuleProfileRightServiceImpl moduleProfileRightService;

    @Autowired
    private ModuleServiceImpl moduleService;

    @Autowired
    private RightServiceImpl rightService;

    @Autowired
    UserAuthServiceImpl userAuthService;

    Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Override
    public List<Profile> findAll() {
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
    public Profile save(Profile entity) {
        return repository.save(entity);
    }

    @Override
    public ICommonRepository<Profile> getRepository() {
        return repository;
    }

    @Override
    public List<Profile> getAll() {
        return getRepository().findAllByDeletedIsNull().parallelStream()
                .peek(profile -> profile.setWorkspace(workspaceService.getByCode(profile.getWorkspaceCode()).orElse(null)))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Initializes the default data for the application.
     *
     * <p>This method is responsible for setting up the default data required
     * by the application. It performs the following actions:
     * <ul>
     *   <li>Ensures default rights are created if they do not exist.</li>
     *   <li>Ensures default modules are created if they do not exist.</li>
     *   <li>Creates default profiles if they do not already exist.</li>
     *   <li>Creates default module rights if they do not already exist.</li>
     * </ul>
     *
     * <p>This method should be called during application startup to ensure
     * that all necessary default data is present in the system.
     */
    public void initializeDefaultData() {
        logger.info("start initializeDefaultData ...");
        rightService.createDefaultRightsIfNotExist();
        moduleService.createDefaultModulesIfNotExist();
        createDefaultProfile();
        moduleProfileRightService.createDefaultModuleRight();
    }

    /**
     * This method is used to initialize the default data for the application.
     * It checks if the default data already exists in the database and if not, it creates them.
     */
    @Override
    public void createDefaultProfile() {
        logger.info("CREATE_DEFAULT_PROFILE :: start createDefaultProfile ... count {}", repository.findAllByDeletedIsNull().size());
        if (repository.findAllByDeletedIsNull().isEmpty()) {
            logger.info("CREATE_DEFAULT_PROFILE :: create default profile");
            List<Profile> defaultProfiles = DefaultSystemRight.getAll().stream()
                    .map(defaultRight -> {
                        Profile profile = new Profile();
                        profile.setAutoFields();
                        profile.setName(defaultRight.toString());
                        profile.setDefault(true);
                        profile.setUpdateBy(userAuthService.getLoggedUserId());
                        return profile;
                    })
                    .collect(Collectors.toList());
            repository.saveAll(defaultProfiles);
        }
        logger.info("CREATE_DEFAULT_PROFILE :: end createDefaultProfile ...");
    }

    /**
     * Retrieves a profile by its name and if it is default.
     *
     * @param name the name of the profile
     * @return the profile if found, null otherwise
     */
    public Profile getByNameAndIsDefault(String name) {
        return repository.findByNameAndIsDefaultIsTrue(name).orElse(null);
    }
}
