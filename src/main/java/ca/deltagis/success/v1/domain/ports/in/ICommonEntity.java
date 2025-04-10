/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-25 13:24:42
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/ports/in/ICommonEntity.java
 * @Description: This is the default configuration, you can modify it in Settings > Tools > File Description.
 */
package ca.deltagis.success.v1.domain.ports.in;


import ca.deltagis.success.v1.domain.ports.in.logs.LogScope;
import ca.deltagis.success.v1.domain.ports.in.logs.LogsTable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@LogsTable(scope = LogScope.METADATA, eventType = {"create", "update", "delete"})
public interface ICommonEntity<T> extends Comparable<T>, Serializable {

    /**
     * Retrieves the internal unique ID of the entity as used in the database.
     *
     * @return the unique identifier of the entity.
     */
    Long getId();

    /**
     * Retrieves the unique UUID of the entity.
     *
     * @return the UUID of the entity.
     */
    UUID getUuid();

    /**
     * Retrieves a user-friendly display name of the entity.
     *
     * @return the display name of the entity.
     */
    String getDisplayName();

    /**
     * Retrieves the creation timestamp of the entity.
     *
     * @return the creation date and time.
     */
    Timestamp getCreated();

    /**
     * Retrieves the timestamp of the last update to the entity.
     *
     * @return the last updated date and time.
     */
    Timestamp getUpdated();

    /**
     * Retrieves the ID of the user who last updated the entity.
     *
     * @return the identifier of the user who last updated the entity.
     */
    Long getUpdateBy();

    /**
     * Retrieves the deletion timestamp of the entity, if it has been deleted.
     *
     * @return the deletion date and time, or null if not deleted.
     */
    Timestamp getDeleted();

    /**
     * Sets the creation timestamp of the entity.
     *
     * @param date the creation date and time.
     */
    void setCreated(Timestamp date);

    /**
     * Sets the last update timestamp of the entity.
     *
     * @param date the last updated date and time.
     */
    void setUpdated(Timestamp date);

    /**
     * Sets the ID of the user who last updated the entity.
     *
     * @param id the identifier of the user who last updated the entity.
     */
    void setUpdateBy(Long id);

    /**
     * Sets the deletion timestamp of the entity.
     *
     * @param date the deletion date and time.
     */
    void setDeleted(Timestamp date);

    /**
     * Sets the internal unique ID of the entity as used in the database.
     *
     * @param id the unique identifier of the entity.
     */
    // void setIdRecord(long id);

    /**
     * Automatically sets common fields such as creation, update, and deletion timestamps.
     * This method is intended to be called when initializing or updating the entity.
     */
    void setAutoFields();

    /**
     * Provides a string representation of the entity.
     *
     * @return a string representing the entity.
     */
    String toString();

    /**
     * Compares this entity with another object for order.
     *
     * @param object the object to be compared with.
     * @return a negative integer, zero, or a positive integer as this entity is less than,
     * equal to, or greater than the specified object.
     */
    int compareTo(T object);
}
