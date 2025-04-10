/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-08 14:20:56
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-10 14:43:56
 */
package ca.deltagis.success.v1.domain.core.models.rigths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum DefaultSystemRight {
    SUPER_ADMIN,
    ADMIN,
    USER,
    GUEST,
    NO_ACCESS;

    private String code;

    private final String name;

    public String DefaultSystemRight() {
        return code;
    }

    DefaultSystemRight() {
        this.name = name();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Retrieves all the default system rights in the system.
     *
     * @return a list of all the default system rights.
     */
    public static List<DefaultSystemRight> getAll() {
        return new ArrayList<>(Arrays.asList(DefaultSystemRight.values()));
    }

    /**
     * Retrieves a default system right by its code.
     *
     * @param value the code of the default system right to be retrieved.
     * @return the default system right with the specified code, or
     * {@link DefaultSystemRight#USER} if no such right exists.
     */
    public static DefaultSystemRight getByValue(String value) {
        return Arrays
                .stream(DefaultSystemRight.values())
                .filter(r -> r.code.equals(value))
                .findFirst()
                .orElse(USER);
    }

    /**
     * Retrieves a default system right by its name.
     *
     * @param value the name of the default system right to be retrieved.
     * @return the default system right with the specified name, or
     * {@link DefaultSystemRight#USER} if no such right exists.
     */
    public static DefaultSystemRight fromString(String value) {
        return Arrays
                .stream(DefaultSystemRight.values())
                .filter(r -> r.name.equals(value))
                .findFirst()
                .orElseGet(() -> fromString(value));
    }

}
