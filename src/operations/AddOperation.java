package operations;

import model.Location;
import model.LogEntry;
import model.Product;
import java.util.List;
import java.util.Map;

public class AddOperation implements StorageOperation {
    private final Product  product;
    private final Location location;

    public AddOperation(Product product, Location location) {
        this.product = product;
        this.location = location;
    }

    @Override
    public String execute(Map<String, List<Product>> stock, List<LogEntry> log) {
        return "";
    }
}