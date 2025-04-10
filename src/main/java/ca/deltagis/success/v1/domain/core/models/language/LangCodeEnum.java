package ca.deltagis.success.v1.domain.core.models.language;

public enum LangCodeEnum {
    EN("en"),
    FR("fr"),
    ES("es");

    private final String code;

    LangCodeEnum(String code) {
        this.code = code;
    }
}
