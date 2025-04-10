package ca.deltagis.success.v1.infrastructure.schema;

public enum PropertyType {
    IDENTIFIER,
    TEXT,
    NUMBER,
    INTEGER,
    BOOLEAN,
    USERNAME,
    EMAIL,
    PASSWORD,
    URL,
    DATE,
    PHONENUMBER,
    GEOLOCATION,
    COLOR,
    CONSTANT,

    COMPLEX,
    COLLECTION,
    REFERENCE;

    public boolean isSimple() {
        return IDENTIFIER == this || TEXT == this || NUMBER == this || INTEGER == this || BOOLEAN == this
                || USERNAME == this || EMAIL == this || PASSWORD == this || URL == this || DATE == this
                || PHONENUMBER == this || GEOLOCATION == this || COLOR == this || CONSTANT == this;
    }
    
}
