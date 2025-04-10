/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-26 09:19:40
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-26 09:19:40
 */
package ca.deltagis.success.v1.application.validator.in;

public interface RightStringValidator {
    /**
     * Checks if the specified rights string is valid.
     * <p>
     * The string should only contain alphanumeric characters, spaces and the following special characters: -_.,:
     *
     * @param right the string to check.
     * @return true if the string is valid, false otherwise.
     */
    boolean isValid(String right);
}
