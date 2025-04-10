package ca.deltagis.success.v1.application.services.exercice;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ca.deltagis.success.v1.adapters.web.http.exercice.request.ExerciceSaveResponse;
import ca.deltagis.success.v1.adapters.web.http.project.request.ProjectSaveResponse;
import ca.deltagis.success.v1.adapters.web.message.ExerciceMessage;
import ca.deltagis.success.v1.adapters.web.message.ProjectMessage;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.exercice.Exercice;
import ca.deltagis.success.v1.domain.core.entities.project.Project;
import ca.deltagis.success.v1.domain.core.entities.project.specificTaxes.ProjectSpecificTaxes;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.models.exercice.ExerciceStatus;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.exercice.IExerciceService;
import ca.deltagis.success.v1.domain.ports.in.logs.Logs;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.exercice.ExerciceRepository;
import ca.deltagis.success.v1.infrastructure.repository.periode.PeriodeRepository;
import ca.deltagis.success.v1.infrastructure.repository.project.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Service
public class ExerciceServiceImpl extends AbstractCommonService<Exercice> implements IExerciceService {

    @Autowired
    private ExerciceRepository repository;
    @Autowired
    private ProjectRepository repositoryProject;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Override
    public List<Exercice> findAll() {
        return repository.findAll();
    }

    @Override
    public Exercice getById(Long id) {
        Exercice findExercice = repository.findById(id).orElse(null);
        if (findExercice != null) {
            findExercice.setOwner(userService.getOptionalUserById(findExercice.getId()).orElse(null));

        }
        return findExercice;
    }

    @Override
    public String delete(Exercice exercice) {
        setCurrentUser();

        try {

            Exercice existingExercice = repository.findById(exercice.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Exercice not found"));

            if (existingExercice.getStatus() == ExerciceStatus.PENDING) {
                existingExercice.setStatus(ExerciceStatus.DELETED);
                exercice.setDeleted(new Timestamp(System.currentTimeMillis()));
                existingExercice.setUpdateBy(exercice.getUpdateBy());
                existingExercice.setDeleted(exercice.getDeleted());
                repository.save(existingExercice);

                return "Exercice with ID " + exercice.getId() + " successfully deleted.";
            }

            return "Exercice cannot be deleted because its status is not PENDING.";
        } catch (EntityNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "An unexpected error occurred during the delete operation.";
        }
    }

    @Override
    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public Exercice save(Exercice entity) {
        return repository.save(entity);
    }

    @Override
    public ICommonRepository<Exercice> getRepository() {
        return repository;
    }

    @Override
    public List<Exercice> getAll() {
        return getRepository()
                .findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .peek(exercice -> exercice
                        .setOwner(userService.getOptionalUserById(exercice.getId()).orElse(null)))

                .toList();
    }

    public Exercice getByIdExercice(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("exercice not found"));
    }

    @Override
    public Optional<Exercice> getByCode(String code) {
        return repository.findByCode(code);

    }

    public ExerciceSaveResponse saveExercice(Exercice exercice) {
        try {

            String exerciceCode = exercice.getCode();
            Project project = repositoryProject.findByCode(exerciceCode)
                    .orElseThrow(() -> new RuntimeException("Project not found with code: " + exerciceCode));
            Optional<Exercice> lastExercice = repository.findFirstByOrderByStartDayAsc();

            if (project.getStartDate().compareTo(exercice.getStartDay()) <= 0 &&
                    project.getEndDate().compareTo(exercice.getEndDay()) >= 0) {
                return new ExerciceSaveResponse(exercice, ExerciceMessage.EXERCICE_NOT_INCLUD, null);

            }
            if (lastExercice.isPresent() &&
                    exercice.getStartDay().compareTo(lastExercice.get().getStartDay()) < 0) {
                return new ExerciceSaveResponse(exercice, ExerciceMessage.LATER_EXERCISES, null);

            }
            if (lastExercice.isPresent()) {
                LocalDate lastStartDate = lastExercice.get().getStartDay().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate();
                LocalDate newStartDate = exercice.getStartDay().toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (newStartDate.isAfter(lastStartDate.plusYears(1))) {

                    return new ExerciceSaveResponse(null, ExerciceMessage.INVALID_START_DATE, null);
                }
            }

            return repository.findByCode(exerciceCode).map(existingExercice -> {
                if (existingExercice.getStatus() == ExerciceStatus.PENDING) {
                    return new ExerciceSaveResponse(existingExercice, ExerciceMessage.ALREADY_PENDING, null);
                } else {
                    return new ExerciceSaveResponse(existingExercice, ExerciceMessage.ALREADY_EXISTS, null);
                }
            }).orElseGet(() -> {

                exercice.setAutoFields();
                exercice.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());

                repository.save(exercice);
                return new ExerciceSaveResponse(exercice, null, null);
            });

        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
            HashMap<String, String> errorDetail = new HashMap<>();
            errorDetail.put("message", e.getMessage());
            return new ExerciceSaveResponse(null, ExerciceMessage.PROJECT_NOT_FOUND, errorDetail);
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> errorDetail = new HashMap<>();
            errorDetail.put("message", e.getMessage());
            return new ExerciceSaveResponse(null, ExerciceMessage.SOMETHING_WENT_WRONG, errorDetail);
        }
    }

    public ExerciceSaveResponse updateExercice(Exercice exercice, String code) {
        try {
            Exercice lastExercice = repository.findByCode(code)
                    .orElseThrow(() -> new EntityNotFoundException("Exercice not found"));

            updateFields(lastExercice, exercice);

            Exercice updatedExercice = repository.save(lastExercice);
            return new ExerciceSaveResponse(updatedExercice, null, null);

        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> errorDetail = new HashMap<>();
            errorDetail.put("message", e.getMessage());
            return new ExerciceSaveResponse(null, ExerciceMessage.SOMETHING_WENT_WRONG, null);
        }
    }

    List<String> updateFields(Exercice oldExercice, Exercice newExercice) {

        List<String> messages = new ArrayList<>();

        if (oldExercice.getStatus() != ExerciceStatus.PENDING) {
            messages.add("Field 'exercice' was not updated to preserve its original value.");
        } else {
            oldExercice.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
            // oldExercice.setLabelPeriode(newExercice.getLabelPeriode());
            oldExercice.setStartDay(newExercice.getStartDay());
            oldExercice.setEndDay(newExercice.getEndDay());
            // oldExercice.setEnd(newExercice.getEnd());
            // oldExercice.setBeginning(newExercice.getBeginning());
            oldExercice.setStatus(newExercice.getStatus());
            oldExercice.setName(newExercice.getName());
            // oldExercice.setNumberPeriode(newExercice.getNumberPeriode());
            // oldExercice.setDeleted(newExercice.getDeleted());
            // oldExercice.setClosing(newExercice.getClosing());

        }
        return messages;

    }

    @Override
    public Long deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
