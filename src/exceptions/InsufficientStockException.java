package exceptions;

/**
 * Thrown when the user tries to remove more stock than is available
 * Carries the available quantity and the unit so the caller can react
 */
public class InsufficientStockException extends RuntimeException {

    /** Available quantity at the moment of the failure */
    private final double available;

    /** Measure unit of the product */
    private final String unit;

    /**
     * Constructor
     * @param productName name of the product
     * @param available how much is actually available
     * @param unit measure unit name
     */
    public InsufficientStockException(String productName, double available, String unit) {
        super("Insufficient stock for: " + productName);
        this.available = available;
        this.unit = unit;
    }

    /**
     * Gets the available quantity
     * @return available quantity
     */
    public double getAvailable() { return available; }

    /**
     * Gets the measure unit name
     * @return unit name
     */
    public String getUnit() { return unit; }
}
