package operations;

import model.Location;
import model.LogEntry;
import model.Product;
import model.Storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Operation that adds a new product batch to the storage
 * If a batch with the same expiry exists, the new one reuses its location
 * Adds a log entry of type ADD
 */
public class AddOperation implements StorageOperation {

    /** The product to add */
    private final Product product;

    /** The location where the product should be placed if there is no same-expiry batch */
    private final Location location;

    /**
     * Constructor
     * @param product the product to add
     * @param location the requested location
     */
    public AddOperation(Product product, Location location) {
        this.product = product;
        this.location = location;
    }

    /**
     * Adds the product to the storage and creates a log entry
     * @param storage the storage to add to
     * @return formatted message describing the result
     */
    @Override
    public String execute(Storage storage) {
        List<Product> existing = storage.getStock()
                .computeIfAbsent(product.getName(), k -> new ArrayList<>());

        Optional<Product> sameExpiry = existing.stream()
                .filter(p -> p.getExpireDate().equals(product.getExpireDate()))
                .findFirst();

        Location assigned = sameExpiry.map(Product::getLocation).orElse(location);
        product.setLocation(assigned);
        existing.add(product);

        storage.getLogEntries().add(new LogEntry(
                LocalDate.now(), LogEntry.Type.ADD,
                product.getName(), product.getQuantity(),
                product.getMeasureUnit(), assigned,
                product.getExpireDate(), ""));

        return String.format("Successfully added %.2f %s of '%s' at %s.",
                product.getQuantity(), product.getMeasureUnit(),
                product.getName(), assigned);
    }
}
