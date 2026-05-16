package exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, double available, String unit) {
        super(String.format("Insufficient stock for '%s'. Available: %.2f %s",
                productName, available, unit));
    }
}
