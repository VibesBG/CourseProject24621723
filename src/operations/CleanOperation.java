package operations;

import commands.CommandParser;
import model.LogEntry;
import model.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CleanOperation implements StorageOperation {
    private final int daysThreshold;
    private final String lossProductName;
    private final double lossPrice;
    private final LocalDate lossFrom;
    private final LocalDate lossTo;

    public CleanOperation(int daysThreshold) {
        this(daysThreshold, null, 0.0, null, null);
    }

    public CleanOperation(int daysThreshold, String lossProductName, double lossPrice, LocalDate lossFrom, LocalDate lossTo) {
        this.daysThreshold = daysThreshold;
        this.lossProductName = lossProductName;
        this.lossPrice = lossPrice;
        this.lossFrom = lossFrom;
        this.lossTo = lossTo;
    }

    @Override
    public String execute(Map<String, List<Product>> stock, List<LogEntry> log) {
        return "";
    }
}