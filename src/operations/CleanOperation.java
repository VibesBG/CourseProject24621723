package operations;

import model.LogEntry;
import model.Product;
import model.Storage;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Operation that removes products which are expired or expire within N days
 * Optionally calculates the total money lost for one product over a period
 */
public class CleanOperation implements StorageOperation {

    /** Number of days from today within which products are considered to expire soon */
    private final int daysThreshold;

    /** Product name to calculate losses for, null if not requested */
    private final String lossProductName;

    /** Price per unit used in the loss calculation */
    private final double lossPrice;

    /** Start of the loss period, null if not requested */
    private final LocalDate lossFrom;

    /** End of the loss period, null if not requested */
    private final LocalDate lossTo;

    /**
     * Constructor for plain cleaning without loss calculation
     * @param daysThreshold expiry threshold in days
     */
    public CleanOperation(int daysThreshold) {
        this(daysThreshold, null, 0.0, null, null);
    }

    /**
     * Constructor with loss calculation
     * @param daysThreshold expiry threshold in days
     * @param lossProductName product to calculate losses for
     * @param lossPrice price per unit
     * @param lossFrom start of the loss period
     * @param lossTo end of the loss period
     */
    public CleanOperation(int daysThreshold, String lossProductName,
                          double lossPrice, LocalDate lossFrom, LocalDate lossTo) {
        this.daysThreshold = daysThreshold;
        this.lossProductName = lossProductName;
        this.lossPrice = lossPrice;
        this.lossFrom = lossFrom;
        this.lossTo = lossTo;
    }

    /**
     * Goes through the stock and removes expiring batches
     * Writes a CLEAN log entry for each removed batch and builds a report
     * @param storage the storage to clean
     * @return formatted report with the cleaned products and optional loss summary
     */
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

                if (p.getName().equalsIgnoreCase(lossProductName)
                        && lossFrom != null
                        && !p.getExpireDate().isBefore(lossFrom)
                        && !p.getExpireDate().isAfter(lossTo)) {
                    totalLoss += p.getQuantity() * lossPrice;
                    lossUnit = p.getMeasureUnit().name();
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
