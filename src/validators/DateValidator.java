package validators;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidator {
    public static LocalDate parse(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date '" + dateStr + "'. Use YYYY-MM-DD.");
        }
    }

    public static void validateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to))
            throw new IllegalArgumentException(
                    "Start date cannot be after end date.");
    }
}