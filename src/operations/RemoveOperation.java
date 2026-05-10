package operations;

import model.LogEntry;
import model.Product;
import java.util.List;
import java.util.Map;

public class RemoveOperation implements  StorageOperation {
    private final String productName;
    private final double quantityToRemove;

    public RemoveOperation(String productName, double quantityToRemove) {
        this.productName = productName;
        this.quantityToRemove = quantityToRemove;
    }


    @Override
    public String execute(Map<String, List<Product>> stock, List<LogEntry> log) {
        return "";
    }
}