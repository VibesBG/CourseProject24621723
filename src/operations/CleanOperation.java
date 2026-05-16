package operations;

import model.LogEntry;
import model.Product;
import model.Storage;

import java.time.LocalDate;
import java.util.Iterator;
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

    public CleanOperation(int daysThreshold, String lossProductName,
                          double lossPrice, LocalDate lossFrom, LocalDate lossTo) {
        this.daysThreshold = daysThreshold;
        this.lossProductName = lossProductName;
        this.lossPrice = lossPrice;
        this.lossFrom = lossFrom;
        this.lossTo = lossTo;
    }

    @Override
    public String execute(Storage storage) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Cleaning batches expiring within %d day(s):%n", daysThreshold));
        sb.append("-".repeat(65)).append("\n");

        boolean anyRemoved = false;
        double totalLoss = 0.0;
        String lossUnit = "";

        for (Iterator<Map.Entry<String, List<Product>>> mapIt =
             storage.getStock().entrySet().iterator(); mapIt.hasNext(); ) {

            Map.Entry<String, List<Product>> entry = mapIt.next();

            for (Iterator<Product> it = entry.getValue().iterator(); it.hasNext(); ) {
                Product p = it.next();
                if (!p.expiresSoon(daysThreshold)) continue;

                sb.append(String.format("  REMOVED  %-20s  %.2f %-6s  expiry: %s  loc: %s%n",
                        p.getName(), p.getQuantity(), p.getMeasureUnit(),
                        p.getExpireDate(), p.getLocation()));

                storage.getLogEntries().add(new LogEntry(
                        LocalDate.now(), LogEntry.Type.CLEAN,
                        p.getName(), p.getQuantity(), p.getMeasureUnit(),
                        p.getLocation(), p.getExpireDate(),
                        "Cleaned – expiring within " + daysThreshold + " days"));

                if (lossProductName != null
                        && p.getName().equalsIgnoreCase(lossProductName)
                        && lossFrom != null
                        && !p.getExpireDate().isBefore(lossFrom)
                        && !p.getExpireDate().isAfter(lossTo)) {
                    totalLoss += p.getQuantity() * lossPrice;
                    lossUnit = p.getMeasureUnit();
                }

                it.remove();
                anyRemoved = true;
            }

            if (entry.getValue().isEmpty()) mapIt.remove();
        }

        if (!anyRemoved) sb.append("  Nothing to clean.\n");

        if (lossProductName != null && anyRemoved) {
            sb.append(String.format("%nLoss report for '%s':%n", lossProductName));
            sb.append(String.format("  Price / %-6s : %.2f%n",
                    lossUnit.isBlank() ? "unit" : lossUnit, lossPrice));
            sb.append(String.format("  Total loss    : %.2f BGN%n", totalLoss));
        }

        return sb.toString();
    }
}