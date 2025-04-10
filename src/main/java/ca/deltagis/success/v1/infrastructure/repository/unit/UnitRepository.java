package ca.deltagis.success.v1.infrastructure.repository.unit;


import java.util.Optional;


import ca.deltagis.success.v1.domain.core.entities.unit.Unit;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;

public interface UnitRepository extends ICommonRepository<Unit> {

    /**
     * Retrieves a unit by its code.
     *
     * @param code the code of the unit to find.
     * @return an Optional containing the unit if found, or empty if not found.
     */
    Optional<Unit> findByCode(String code);
}
