package operations;

import model.LogEntry;
import model.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class LogOperation implements StorageOperation {
    private final LocalDate from;
    private final LocalDate to;

    public LogOperation(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String execute(Map<String, List<Product>> stock, List<LogEntry> log) {
        return "";
    }
}