package com.erastedev.ciexplore.v1.infrastructure.repository.currency;

import java.util.Optional;


import com.erastedev.ciexplore.v1.domain.entities.currency.Currency;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;

public interface CurrencyRepository extends ICommonRepository<Currency> {

    boolean existsByCode(String code);

    Optional<Currency> findByCode(String code);
}
