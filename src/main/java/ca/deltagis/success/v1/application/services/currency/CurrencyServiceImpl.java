package ca.deltagis.success.v1.application.services.currency;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ca.deltagis.success.v1.application.services.files.FilePathManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import ca.deltagis.success.v1.adapters.web.http.currency.request.CurrencySaveResponse;

import ca.deltagis.success.v1.adapters.web.message.CurrencyMessage;

import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;

import ca.deltagis.success.v1.domain.core.entities.currency.Currency;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.currency.IcurrencyService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.currency.CurrencyRepository;
import lombok.Getter;
import lombok.Setter;

@Slf4j
@Getter
@Setter
@Service
public class CurrencyServiceImpl extends AbstractCommonService<Currency> implements IcurrencyService {

    @Autowired
    private CurrencyRepository repository;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    FilePathManager pathManager;

    private String jsonPath;

    CurrencyServiceImpl(FilePathManager _pathManager) {
        this.pathManager = _pathManager;
        jsonPath = pathManager.fromResource("/data/currencies.json");
    }

    @Override
    public List<Currency> findAll() {
        return repository.findAll();
    }

    /**
     * Retrieves a company by its ID.
     *
     * @param id the ID of the company to be retrieved.
     * @return the company with the specified ID, or null if no such company exists.
     */
    @Override
    public Currency getById(Long id) {
        return repository.findById(id).orElse(null);
    }

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
    public Currency save(Currency entity) {
        return repository.save(entity);
    }

    @Override
    public void importCurrencies() {
        Map<String, String> json = readJsonFileCollection();
        for (Map.Entry<String, String> entry : json.entrySet()) {
            if (!repository.existsByCode(entry.getKey())) {
                Currency currency = Currency.builder().name(entry.getValue()).code(entry.getKey()).build();
                currency.setAutoFields();
                currency.setUpdateBy(userAuthService.getLoggedUserId());
                repository.save(currency);
            }
        }
    }

    @Override
    public List<Currency> getAllCurrencies() {
        if (repository.findAll().isEmpty()) {
            importCurrencies();
        }

        return repository.findAll();
    }

    public boolean fileExists(String path) {
        return Files.exists(Path.of(path));
    }

    public Map<String, String> readJsonFileCollection() {
        try {
            boolean checkFile = fileExists(jsonPath);
            logger.info("Check Countries file exist: {}");

            if (checkFile) {
                File countriesFile = new File(jsonPath);
                Path path = countriesFile.toPath();
                byte[] jsonData = Files.readAllBytes(path);

                ObjectMapper objectMapper = new ObjectMapper();
                MapType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class);
                Map<String, String> jsonMap = objectMapper.readValue(jsonData, mapType);

                return jsonMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
