package ca.deltagis.success.v1.domain.common.port;

import java.io.Serializable;

public interface PrimaryKeyObject extends Serializable {
    /**
     * @return internal unique ID of the object as used in the database
     */
    long getId();

    /**
     * @return external unique ID of the object as used in the RESTful API
     */
    String getUid();
}