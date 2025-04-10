package ca.deltagis.success.v1.application.services.billetage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ca.deltagis.success.v1.domain.core.entities.currency.Currency;
import ca.deltagis.success.v1.domain.core.entities.exercice.Exercice;
import ca.deltagis.success.v1.adapters.web.http.billetage.request.BilletageSaveResponse;
import ca.deltagis.success.v1.adapters.web.http.exercice.request.ExerciceSaveResponse;
import ca.deltagis.success.v1.adapters.web.message.BilletageMessage;
import ca.deltagis.success.v1.adapters.web.message.ExerciceMessage;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.billetage.Billetage;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.Billetage.IbilletageService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.billetage.BilletageRepository;
import ca.deltagis.success.v1.infrastructure.repository.currency.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class BilletageServiceImpl extends AbstractCommonService<Billetage> implements IbilletageService {

    @Autowired
    private BilletageRepository billetageRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Override
    public List<Billetage> findAll() {
        return billetageRepository.findAll();
    }

    @Override
    public Long deleteById(Long id) {
        try {
            Billetage billetage = billetageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Billetage not found"));
            billetageRepository.deleteById(id);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existsById(Long id) {
        return billetageRepository.findById(id).isPresent();
    }

    @Override
    public Billetage save(Billetage entity) {
        return billetageRepository.save(entity);
    }

    @Override
    public ICommonRepository<Billetage> getRepository() {
        return billetageRepository;
    }

    @Override
    public List<Billetage> getAll() {
        return getRepository()
                .findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .peek(billetage -> billetage
                        .setOwner(userService.getOptionalUserById(billetage.getId()).orElse(null)))

                .toList();
    }

    public Billetage getByIdBilletage(Long id) {
        return billetageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("billetage not found"));
    }

    public Optional<Billetage> getByCode(String code) {
        return billetageRepository.findByCurrencyCode(code);

    }

    public BilletageSaveResponse saveBilletage(Billetage billetage) {
        String currencyCode = billetage.getCurrencyCode();

        Currency currency = currencyRepository.findByCode(currencyCode).orElse(null);
        if (currency == null) {
            return new BilletageSaveResponse(billetage, BilletageMessage.NOT_FOUND, null);
        }

        billetage.setAutoFields();
        billetage.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());

        if (billetage.getValeur() == null || billetage.getValeur() <= 0) {
            return new BilletageSaveResponse(billetage, BilletageMessage.INVALID_VALUE, null);
        }

        billetage.setDescription(
                String.format("%s de %s %s", billetage.getType(), billetage.getValeur(), currencyCode));

        billetageRepository.save(billetage);

        return new BilletageSaveResponse(billetage, null, null);
    }

    public BilletageSaveResponse updateBilletage(Billetage billetage, Long id) {
        // Currency currency = currencyRepository.findByCode(currencyCode).orElse(null);

        // if (currency == null) {
        // return new BilletageSaveResponse(billetage,
        // BilletageMessage.CURRENCY_NOT_FOUND, null);
        // }
        try {

            Billetage lastBilletage = billetageRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Billetage not found"));
            updateFields(lastBilletage, billetage);
            Billetage billetageUpdated = billetageRepository.save(lastBilletage);
            return new BilletageSaveResponse(billetageUpdated, BilletageMessage.UPDATED, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new BilletageSaveResponse(billetage, BilletageMessage.SOMETHING_WENT_WRONG, null);
        }

    }

    void updateFields(Billetage oldBilletage, Billetage newBilletage) {
        if (newBilletage != null) {
            oldBilletage.setType(newBilletage.getType());
            oldBilletage.setValeur(newBilletage.getValeur());

            String description = String.format("%s de %s %s",
                    oldBilletage.getType(),
                    oldBilletage.getValeur(),
                    oldBilletage.getCurrencyCode());
            oldBilletage.setDescription(description);
        }

    }
}
