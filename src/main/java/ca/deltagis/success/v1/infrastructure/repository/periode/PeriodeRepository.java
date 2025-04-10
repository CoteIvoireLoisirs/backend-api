package ca.deltagis.success.v1.infrastructure.repository.periode;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.deltagis.success.v1.domain.core.entities.periode.Periode;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;

@Repository
public interface PeriodeRepository extends ICommonRepository<Periode>{

    
    /**
     * Retrieves a periode by its code.
     *
     * @param code the code of the workspace to find.
     * @return an Optional containing the workspace if found, or empty if not found.
     */
    Optional<Periode> findByCode(String code);

}
