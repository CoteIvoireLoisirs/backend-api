package ca.deltagis.success.v1.domain.ports.in.unit;

import java.util.Optional;


import ca.deltagis.success.v1.adapters.web.http.unit.UnitedSaveResponse;
import ca.deltagis.success.v1.domain.core.entities.unit.Unit;

public interface IUnitService {

    /**
     * Retrieves a unit by its code.
     *
     * @param code the code of the unit to find.
     * @return an Optional containing the unit if found, or empty if not found.
     */
    Optional<Unit> getByCode(String code);
}
