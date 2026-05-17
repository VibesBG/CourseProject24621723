package operations;

import model.Product;
import model.Storage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Operation that prints all products in a table
 * Quantities of all batches of the same product are summed
 * Unique locations are listed in one column
 */
public class PrintOperation implements StorageOperation {

    /**
     * Default constructor
     */
    public PrintOperation() {
    }

    /**
     * Builds a table with the current state of the warehouse
     * @param storage the storage to read from
     * @return formatted table or a message that the warehouse is empty
     */
    @Override
    public String execute(Storage storage) {
        if (storage.getStock().isEmpty()) return "The warehouse is empty.";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-22s %-10s %-8s %-16s %-10s %-12s%n",
                "Name", "Total Qty", "Unit", "Manufacturer", "Price/unit", "Location(s)"));
        sb.append("-".repeat(85)).append("\n");

        for (Map.Entry<String, List<Product>> entry : storage.getStock().entrySet()) {
            List<Product> list = entry.getValue();
            double total = list.stream().mapToDouble(Product::getQuantity).sum();
            Product first = list.getFirst();
            String locs = list.stream()
                    .map(p -> p.getLocation().toString())
                    .distinct()
                    .collect(Collectors.joining(", "));

            sb.append(String.format("%-22s %-10.2f %-8s %-16s %-10.2f %-12s%n",
                    first.getName(), total, first.getMeasureUnit(),
                    first.getManufacturerName(), first.getPricePerUnit(), locs));
        }
        return sb.toString();
    }
}
