package exceptions;

/**
 * Thrown when an operation is requested for a product that is not in the storage
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructor
     * @param productName name of the missing product
     */
    public ProductNotFoundException(String productName) {
        super("Product not found: " + productName);
    }
}
