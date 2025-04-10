package ca.deltagis.success.v1.adapters.web.http.project.request;

import java.util.Currency;
import java.util.HashMap;

import ca.deltagis.success.v1.adapters.web.message.CurrencyMessage;

public class CurreciesResponse {
    private Currency currency;
    private CurrencyMessage error;
    private HashMap<String, String> errorDetail = null;

}
