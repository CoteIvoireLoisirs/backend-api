package com.erastedev.ciexplore.v1.adapters.web.http.currency.request;

import java.util.HashMap;

import com.erastedev.ciexplore.v1.adapters.web.message.CurrencyMessage;
import com.erastedev.ciexplore.v1.domain.entities.currency.Currency;
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
