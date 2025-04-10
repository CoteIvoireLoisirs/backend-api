package com.erastedev.ciexplore.v1.adapters.web.message;

public enum BilletageMessage {
    OK("OK"),
    CREATED ("YOUR BILLETAGE HAS BEEN CREATED"),
    NOT_FOUND("CURRENCY NOT FOUND"),
    SOMETHING_WENT_WRONG("SOMETHING WENT WRONG"),
    UPDATED("BILLETAGE UPDATED"),
    CURRENCY_NOT_FOUND("CURRENCY NOT FOUND"),
    DELETED("BILLETAGE HAS BEEN DELETED"),
    INVALID_OPERATION("operation impossible"),
    NOT_UPDATED("MODIFICATION IMPOSSIBLE "),
    INVALID_VALUE("INVALID VALUE"),
    NONE("NONE");







    private final String value;
    
    BilletageMessage(String value) {
        this.value = value;
    }


}
