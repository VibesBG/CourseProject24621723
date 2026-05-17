package operations;

import model.LogEntry;
import model.Product;
import model.Storage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Operation that removes a quantity of a product from the storage
 * Batches with the closest expiry date are removed first
 * Throws if the product doesn't exist or if the quantity is more than available
 */
public class RemoveOperation implements StorageOperation {

    /** Name of the product to remove */
    private final String productName;

    /** Total quantity to remove */
    private final double quantityToRemove;

    /**
     * Constructor
     * @param productName product name
     * @param quantityToRemove quantity to remove
     */
    public RemoveOperation(String productName, double quantityToRemove) {
        this.productName = productName;
        this.quantityToRemove = quantityToRemove;
    }

    /**
     * Removes the requested quantity and writes log entries
     * @param storage the storage to remove from
     * @return formatted message listing the batches that were touched
     * @throws exceptions.ProductNotFoundException if the product does not exist
     * @throws exceptions.InsufficientStockException if there is not enough stock
     */
    @Override
    public String execute(Storage storage) {
        List<Product> list = storage.getStock().get(productName);
        if (list == null || list.isEmpty())
            throw new exceptions.ProductNotFoundException(productName);

        double available = list.stream().mapToDouble(Product::getQuantity).sum();

        if (quantityToRemove > available) {
            throw new exceptions.InsufficientStockException(productName, available, list.getFirst().getMeasureUnit().name());
        }

        list.sort(Comparator.comparing(Product::getExpireDate));

        StringBuilder summary = new StringBuilder();
        summary.append(String.format("Removed %.2f %s of '%s':\n",
                quantityToRemove, list.getFirst().getMeasureUnit(), productName));

        double remaining = quantityToRemove;
        for (Iterator<Product> it = list.iterator(); it.hasNext() && remaining > 0;) {
            Product p = it.next();
            double take = Math.min(p.getQuantity(), remaining);
            remaining -= take;

            summary.append(String.format("  %.2f from %s (expiry: %s)\n",
                    take, p.getLocation(), p.getExpireDate()));

            storage.getLogEntries().add(new LogEntry(
                    LocalDate.now(), LogEntry.Type.REMOVE,
                    productName, take, p.getMeasureUnit(),
                    p.getLocation(), p.getExpireDate(), ""));

            if (take >= p.getQuantity()) it.remove();
            else p.setQuantity(p.getQuantity() - take);
        }

        if (list.isEmpty()) storage.getStock().remove(productName);
        return summary.toString();
    }
}
