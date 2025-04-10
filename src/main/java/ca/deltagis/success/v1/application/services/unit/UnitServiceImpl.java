package ca.deltagis.success.v1.application.services.unit;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ca.deltagis.success.v1.adapters.web.http.unit.UnitedSaveResponse;
import ca.deltagis.success.v1.adapters.web.message.UnitedMessage;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.project.Project;
import ca.deltagis.success.v1.domain.core.entities.unit.Unit;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.unit.IUnitService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.project.ProjectRepository;
import ca.deltagis.success.v1.infrastructure.repository.unit.UnitRepository;

@Service
public class UnitServiceImpl extends AbstractCommonService<Unit> implements IUnitService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Autowired
    private UnitRepository repository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Unit> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public Unit save(Unit entity) {
        return repository.save(entity);
    }

    /**
     * Creates a new unit entity if it does not already exist in the repository.
     *
     * @param entity the unit entity to be created.
     * @return the created unit entity if successful, or null if a unit with the
     *         same code already exists.
     */

    public UnitedSaveResponse createNewUnit(Unit entity) {
        Project project = projectRepository.findByCode(entity.getProjectCode())
                .orElseThrow(() -> new RuntimeException("project with this code not found"));

        if (repository.findByCode(entity.getCode()).isPresent()) {
          
            return new UnitedSaveResponse(entity, UnitedMessage.ALREADY_EXISTS, null);
        }

        try {
           
            entity.setAutoFields();
            entity.setUpdateBy(userAuthService.getLoggedUserId());
            repository.save(entity);
            return new UnitedSaveResponse(entity, UnitedMessage.CREATED, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new UnitedSaveResponse(entity, UnitedMessage.SOMETHING_WENT_WRONG, null);
        }

    }

    @Override
    public ICommonRepository<Unit> getRepository() {
        return repository;
    }

    public Optional<Unit> getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Long deleteById(Long id) {
        repository.deleteById(id);
        return id;
    }

    @Override
    public List<Unit> getAll() {
        return getRepository().findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}
