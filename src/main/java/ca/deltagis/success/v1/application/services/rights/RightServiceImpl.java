/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-08 13:59:09
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-08 14:00:20
 */
package ca.deltagis.success.v1.application.services.rights;

import ca.deltagis.success.v1.domain.core.entities.rights.Right;
import ca.deltagis.success.v1.domain.core.models.rigths.DefaultSystemRight;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.rights.IRightService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.rights.RightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RightServiceImpl extends AbstractCommonService<Right> implements IRightService {
    @Autowired
    private RightRepository repository;

    Logger logger = LoggerFactory.getLogger(RightServiceImpl.class);

    @Override
    public List<Right> findAll() {
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
    public Right save(Right entity) {
        return repository.save(entity);
    }

    @Override
    public ICommonRepository<Right> getRepository() {
        return repository;
    }

    /**
     * Retrieves all rights that have not been marked as deleted.
     * <p>
     * This method ensures that default rights are created if they do not exist
     * before fetching the list of rights. It returns a list of rights that have
     * not been marked as deleted from the repository.
     *
     * @return a list of rights that are not marked as deleted.
     */
    @Override
    public List<Right> getAll() {
        try {
            createDefaultRightsIfNotExist();
            return getRepository().findAllByDeletedIsNull();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error creating entity", e);
        }
        return new ArrayList<Right>();
    }

    /**
     * Create default rights in the database if they do not exist.
     * <p>
     * This method is used to populate the database with default rights when the
     * application is first started, and can also be used to reset the default
     * rights if they are deleted.
     */
    @Override
    public void createDefaultRightsIfNotExist() {
        try {
            logger.info("CREATE_DEFAULT_RIGHTS_IF_NOT_EXIST :: start createDefaultRightsIfNotExist ..., count: {}", getRepository().count());
            if (!checkIfRightDefaultExists()) {
                logger.info("CREATE_DEFAULT_RIGHTS_IF_NOT_EXIST :: create default rights");
                List<Right> defaultRights = DefaultSystemRight.getAll().stream()
                        .map(r -> {
                            Right right = new Right();
                            right.setName(r.name());
                            right.setSystemRight(true);
                            right.setAutoFields();
                            return right;
                        })
                        .toList();
                repository.saveAll(defaultRights);
            } 
            logger.info("CREATE_DEFAULT_RIGHTS_IF_NOT_EXIST :: end createDefaultRightsIfNotExist ...");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error createDefaultRightsIfNotExist", e);
        }
    }

    /**
     * Checks if a default right with the specified name exists in the database.
     *
     * @return true if a default right with the specified name exists, false otherwise.
     */
    @Override
    public boolean checkIfRightDefaultExists() {
        return getRepository().count() > 0;
    }

    /**
     * Save a default right in the database.
     * <p>
     * This method will create a new right with the specified name and mark it as
     * a system right. If the right already exists, it will not be updated.
     *
     * @param name the name of the right to be saved.
     */
    public void saveSystemRight(String name) {
        Right right = new Right();
        right.setAutoFields();
        right.setSystemRight(true);
        right.setName(name);
        right.setId(null);
        repository.save(right);
    }

    @Override
    public boolean isSystemRight(Right right) {
        return right.isSystemRight();
    }
}
