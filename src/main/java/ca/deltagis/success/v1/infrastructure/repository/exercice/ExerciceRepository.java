package ca.deltagis.success.v1.infrastructure.repository.exercice;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ca.deltagis.success.v1.domain.core.entities.exercice.Exercice;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;

@Repository
public interface ExerciceRepository extends ICommonRepository<Exercice> {

   
    /**
     * Retrieves a Exercice by its code.
     *
     * @param code the code of the exercice to find.
     * @return an Optional containing the exercice if found, or empty if not found.
     */
    Optional<Exercice> findByCode(String code);
    Optional<Exercice> findFirstByOrderByStartDayAsc();



}
