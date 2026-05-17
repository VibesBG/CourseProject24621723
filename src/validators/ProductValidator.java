package validators;

import java.time.LocalDate;

/**
 * Helper class with a single static method that validates product fields
 */
public class ProductValidator {

    /**
     * Utility class, not meant to be instantiated
     */
    private ProductValidator() {
    }

    /**
     * Validates all required product fields
     * @param name product name
     * @param unit measure unit string
     * @param price price per unit
     * @param quantity quantity
     * @param arrival arrival date
     * @param expiry expiration date
     * @throws IllegalArgumentException if any value is invalid
     */
    public static void validate(String name, String unit,
                                double price, double quantity,
                                LocalDate arrival, LocalDate expiry) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Product name cannot be empty.");
        if (unit == null || unit.isBlank())
            throw new IllegalArgumentException("Unit cannot be empty.");
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative.");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be positive.");
        if (arrival.isAfter(expiry))
            throw new IllegalArgumentException("Arrival date cannot be after expiry date.");
    }
}
