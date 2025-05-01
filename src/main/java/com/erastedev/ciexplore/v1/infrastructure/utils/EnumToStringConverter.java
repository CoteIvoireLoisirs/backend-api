package com.erastedev.ciexplore.v1.infrastructure.utils;

public class EnumToStringConverter {

    /**
     * Converts an enum constant name to a human-readable sentence by transforming
     * it to lowercase, replacing underscores with spaces, and capitalizing the
     * first letter of each word.
     *
     * @param enumValue the enum constant to be converted; cannot be null
     * @return a human-readable sentence as a String
     * @throws IllegalArgumentException if the provided enumValue is null
     */
    public static String enumToSentence(Enum<?> enumValue) {
        if (enumValue == null) {
            throw new IllegalArgumentException("Enum value cannot be null");
        }

        // Convert the enum name to lower case and split by underscores
        String[] words = enumValue.name().toLowerCase().split("_");

        // Capitalize the first letter of each word
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            result.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));
            if (i < words.length - 1) {
                result.append(" "); // Add space between words
            }
        }

        return result.toString();
    }
}
