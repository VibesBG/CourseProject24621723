package validators;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Helper class with static methods for date validation
 */
public class DateValidator {

    /**
     * Utility class, not meant to be instantiated
     */
    private DateValidator() {
    }

    /**
     * Parses a string in YYYY-MM-DD format
     * @param dateStr the string to parse
     * @return parsed LocalDate
     * @throws IllegalArgumentException if the string is not a valid date
     */
    public static LocalDate parse(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date '" + dateStr + "'. Use YYYY-MM-DD.");
        }
    }

    /**
     * Validates that the start date is not after the end date
     * @param from start date
     * @param to end date
     * @throws IllegalArgumentException if from is after to
     */
    public static void validateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to))
            throw new IllegalArgumentException(
                    "Start date cannot be after end date.");
    }
}
