package operations;

import commands.CommandParser;
import model.LogEntry;
import model.Product;

import java.util.List;
import java.util.Map;

public class PrintOperation implements StorageOperation {
    @Override
    public String execute(Map<String, List<Product>> stock, List<LogEntry> log) {
        return "";
    }
}