/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-26 09:20:19
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-26 09:21:57
 */
package ca.deltagis.success.v1.application.validator.out;

import ca.deltagis.success.v1.application.validator.in.RightStringValidator;

import java.util.regex.Pattern;

public class RightStringValidatorImpl implements RightStringValidator {

    // Predefined regex pattern
    private static final String REGEX = "^[A-Z_]+_((---|rwd)|([r-][w-][d-]))\\s*$";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    /**
     * Validates the input rights string.
     *
     * @param right the rights string to validate
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid(String right) {
        if (right == null || right.isEmpty()) {
            return false; // Null or empty string is invalid
        }

        // Split the string into module-right pairs
        String[] rightsArray = right.split(";");
        if (rightsArray.length == 0) {
            return false; // No rights provided
        }

        // Validate each module-right pair
        for (String singleRight : rightsArray) {
            if (!PATTERN.matcher(singleRight).matches()) {
                return false; // Invalid format detected
            }
        }

        return true; // All rights are valid
    }
}
