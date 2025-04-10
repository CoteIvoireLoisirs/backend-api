package ca.deltagis.success.v1.domain.ports.in.currency;

import ca.deltagis.success.v1.domain.core.entities.currency.Currency;

import java.util.List;
import java.util.Map;

public interface IcurrencyService {
    /**
     * Get all currencies.
     *
     * @return List of all currencies.
     */
    List<Currency> getAllCurrencies();

    /**
     * Reads a JSON file located at the classpath root and imports all currencies
     * in the file into the database. If a currency with the same code already
     * exists, it is not overwritten.
     */
    void importCurrencies();

    /**
     * Checks if a file exists at the given path.
     *
     * @param path the path of the file to check
     * @return true if the file exists, otherwise false
     */
    boolean fileExists(String path);

    /**
     * Reads a JSON file and returns a collection of objects.
     *
     * @return a map of objects read from the JSON file or null in case of error
     */
    Map<String, String> readJsonFileCollection();
}
