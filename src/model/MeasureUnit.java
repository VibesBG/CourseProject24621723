package model;

/**
 * Represents the unit of measurement for a product.
 */
public enum MeasureUnit {
    KG, LITRES;

    public static MeasureUnit fromString(String s) {
        return switch (s.toLowerCase().trim()) {
            case "kg", "kilogram", "kilograms" -> KG;
            case "litres", "liters", "l" -> LITRES;
            default -> throw new IllegalArgumentException(
                    "Invalid unit: " + s + ". Use 'kg' or 'litres'.");
        };
    }
}