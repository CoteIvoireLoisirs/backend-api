package com.erastedev.ciexplore.v1.adapters.web.message;

public enum PeriodeMessage {

    ALREADY_EXISTS("THIS PERIODE IS ALREADY EXISTE"),
    NOT_FOUND("NOT_FOUND"),
    STARTED_SUCCESSFULLY("STARTED_SUCCESSFULLY"),
    NONE("NONE"),
    SOMETHING_WENT_WRONG("SOMETHING WENT WRONG"),
    UPDATED("PERIODE UPDATED"),
    NOT_UPDATED("MODIFICATION IMPOSSIBLE"),
    NOT_STARTED("NOT_STARTED");

    private final String value;
    
    PeriodeMessage(String value) {
        this.value = value;
    }
}
