package ca.deltagis.success.v1.infrastructure.repository.billetage;


import java.util.Optional;

import ca.deltagis.success.v1.domain.core.entities.billetage.Billetage;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;

public interface BilletageRepository extends ICommonRepository<Billetage> {

   
    Optional<Billetage> findByCurrencyCode(String code);
    Optional<Billetage>findById(Long id);


}
