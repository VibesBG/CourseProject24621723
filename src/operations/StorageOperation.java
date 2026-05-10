package operations;

import model.LogEntry;
import model.Product;
import java.util.List;
import java.util.Map;

public interface StorageOperation {
    String execute(Map<String, List<Product>> stock, List<LogEntry> log);
}