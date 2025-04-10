package ca.deltagis.success.v1.adapters.web.message;

public enum CurrencyMessage {

    ALREADY_EXISTS(" THE CURRENCY ALREADY EXISTE"),
    OK("OK"),
    CREATED ("YOUR CURRENCY HAS BEEN CREATED"),
    NOT_FOUND("NOT_FOUND"),
    UPDATED("CURRENCY UPDATED"),
    SOMETHING_WENT_WRONG("SOMETHING WENT WRONG");




    private final String value;
    
    CurrencyMessage(String value) {
        this.value = value;
    }
    

}
