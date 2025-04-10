package ca.deltagis.success.v1.infrastructure.repository.currency;

import java.util.Optional;


import ca.deltagis.success.v1.domain.core.entities.currency.Currency;
import ca.deltagis.success.v1.domain.core.entities.exercice.Exercice;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;

public interface CurrencyRepository extends ICommonRepository<Currency> {

    boolean existsByCode(String code);

    Optional<Currency> findByCode(String code);

    

}
