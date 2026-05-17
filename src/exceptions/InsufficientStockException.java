package exceptions;

public class InsufficientStockException extends RuntimeException {

    private final double available;
    private final String unit;

    public InsufficientStockException(String productName, double available, String unit) {
        super("Insufficient stock for: " + productName);
        this.available = available;
        this.unit = unit;
    }

    public double getAvailable() { return available; }

    public String getUnit() { return unit; }
}