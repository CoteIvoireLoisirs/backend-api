package ca.deltagis.success.v1.application.services.periode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.deltagis.success.v1.adapters.web.http.periode.request.PeriodeSaveResponse;
import ca.deltagis.success.v1.adapters.web.message.PeriodeMessage;
import ca.deltagis.success.v1.application.services.project.UserProjectFavoriteImpl;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.exercice.Exercice;
import ca.deltagis.success.v1.domain.core.entities.periode.Periode;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.periode.IPeriodeService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.exercice.ExerciceRepository;
import ca.deltagis.success.v1.infrastructure.repository.periode.PeriodeRepository;




@Service
public class PeriodeServiceImpl extends AbstractCommonService<Periode>implements IPeriodeService  {

    @Autowired
    private PeriodeRepository repository;
    @Autowired
    private ExerciceRepository exerciceRepository;
    @Autowired
    private UserAuthServiceImpl userAuthService;

     Logger logger = LoggerFactory.getLogger(UserProjectFavoriteImpl.class);


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
    public Periode save(Periode entity) {
        return repository.save(entity);
    }

    @Override
   public List<Periode> findAll() {
        return repository.findAll();
    }

     @Override
    public ICommonRepository<Periode> getRepository() {
        return repository;
    }

   
    public Optional<Periode> getByCode(String code) {
        return repository.findByCode(code);
    }


    public Periode getByIdPeriode(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("periode not found"));
    }


   
    public PeriodeSaveResponse savePeriode(Periode periode ){
        String code = periode.getCodeExercice();

        Exercice exercice = exerciceRepository.findByCode(code).orElse(null);

        try {
            return repository
            .findByCode(periode.getCode())
            .map(existingWorkspace -> {
           
                if(periode.getCodeExercice() == exercice.getCode()){

                    return new PeriodeSaveResponse(existingWorkspace,PeriodeMessage.ALREADY_EXISTS,null);
                }
                    return new PeriodeSaveResponse(periode, PeriodeMessage.NOT_STARTED, null);
        }).orElseGet(() -> {

           periode.setAutoFields();
           periode.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
           repository.save(periode);
           return new PeriodeSaveResponse(periode, PeriodeMessage.NONE, null);
        });

            
            
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> errorDetail = new HashMap<>();
            errorDetail.put("message", e.getMessage());
            return new PeriodeSaveResponse(periode, PeriodeMessage.SOMETHING_WENT_WRONG, null);
        }


       


    }

    

    public PeriodeSaveResponse updatePeriode(Periode periode){
        try {

            Periode lastperiode = repository.findById(periode.getId()).orElseThrow(() -> new RuntimeException("periode not found"));
            fillPeriodeFields(lastperiode , periode );
            Periode updatePeriode = repository.save(lastperiode);
             return new PeriodeSaveResponse(updatePeriode, PeriodeMessage.UPDATED, null);
            } catch (Exception e) {
                e.printStackTrace();
                HashMap<String, String> errorDetail = new HashMap<>();
                errorDetail.put("message", e.getMessage());
                return new PeriodeSaveResponse(null, PeriodeMessage.SOMETHING_WENT_WRONG, errorDetail);
            }

    }


    void fillPeriodeFields(Periode oldperiode , Periode newperiode){
        List<String> messages = new ArrayList<>();

        if(newperiode!= null && !oldperiode.getCodeExercice().equals(newperiode)){
        messages.add("Field 'exercice' was not updated to preserve its original value.");

           } 
           else {
            oldperiode.setCodeExercice(oldperiode.getCodeExercice());
    }
      oldperiode.setName(newperiode.getName());
      oldperiode.setClosed(newperiode.getClosed());
      oldperiode.setCode(newperiode.getCode());
      oldperiode.setCreated(newperiode.getCreated());
      oldperiode.setEndDate(newperiode.getEndDate());
      oldperiode.setPeriodeNumber(newperiode.getPeriodeNumber());
      oldperiode.setStartDate(newperiode.getStartDate());
      oldperiode.setType(newperiode.getType());
    





    }








}
