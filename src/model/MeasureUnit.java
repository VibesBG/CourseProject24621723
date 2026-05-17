package model;

/**
 * Enum for unit of measurement
 */
public enum MeasureUnit {

    /** Kilogram */
    KG,

    /** Litres */
    LITRES;

    /**
     * Converts a string into MeasureUnit enum
     * @param s the string to convert
     * @return MeasureUnit enum instance
     * @throws IllegalArgumentException if the unit name is unknown
     */
    public static MeasureUnit fromString(String s) {
        return switch (s.toLowerCase().trim()) {
            case "kg", "kilogram", "kilograms" -> KG;
            case "litres", "liters", "l" -> LITRES;
            default -> throw new IllegalArgumentException(
                    "Invalid unit: " + s + ". Use 'kg' or 'litres'.");
        };
    }
}