package ca.deltagis.success.v1.adapters.web.message;

public enum UnitedMessage {

    CREATED("YOUR UNITE HAS BEEN CREATED"),
    NOT_EMPTY("CAN'T BE EMPTY"),
    ALREADY_EXISTS("YOUR UNIT CODE IS ALREADY EXISTE"),
    NOT_UPDATED("MODIFICATION IMPOSSIBLE"),
    NONE("NONE"),
    SOMETHING_WENT_WRONG("SOMETHING WENT WRONG");


    private final String value;
    
    UnitedMessage(String value) {
        this.value = value;
    }


}
