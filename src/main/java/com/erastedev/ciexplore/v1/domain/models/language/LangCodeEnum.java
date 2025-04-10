package com.erastedev.ciexplore.v1.domain.models.language;

public enum LangCodeEnum {
    EN("en"),
    FR("fr"),
    ES("es");

    private final String code;

    LangCodeEnum(String code) {
        this.code = code;
    }
}
