package operations;

import model.Location;
import model.LogEntry;
import model.Product;
import model.Storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddOperation implements StorageOperation {

    private final Product product;
    private final Location location;

    public AddOperation(Product product, Location location) {
        this.product = product;
        this.location = location;
    }

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