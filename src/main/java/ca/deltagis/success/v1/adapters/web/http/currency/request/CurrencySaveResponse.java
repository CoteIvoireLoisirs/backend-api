package ca.deltagis.success.v1.adapters.web.http.currency.request;

import java.util.HashMap;

import ca.deltagis.success.v1.adapters.web.message.CurrencyMessage;
import ca.deltagis.success.v1.domain.core.entities.currency.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencySaveResponse {

    private Currency currency;

    private CurrencyMessage error;

    private HashMap<String, String> errorDetail = null;



}
