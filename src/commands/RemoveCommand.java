package commands;

import model.Storage;
import operations.RemoveOperation;

import java.util.Scanner;

/**
 * Interactive command that removes a given quantity of a product
 * If the requested quantity is more than available, offers to remove what is left
 */
public class RemoveCommand implements Command {

    /** The storage to remove from */
    private final Storage storage;

    /** Scanner used to read user input */
    private final Scanner scanner;

    /**
     * Constructor
     * @param storage the storage to remove from
     * @param scanner the scanner for user input
     */
    public RemoveCommand(Storage storage, Scanner scanner) {
        this.storage = storage;
        this.scanner = scanner;
    }

    /**
     * Runs the interactive remove command
     * @param args ignored, input is read from the scanner
     * @return result message describing what was removed
     */
    @Override
    public String execute(String[] args) {
        System.out.print("Product name: ");
        String name = scanner.nextLine().trim();
        if (name.isBlank()) return "Error: name cannot be empty.";

        System.out.print("Quantity to remove: ");
        double quantity;
        try {
            quantity = Double.parseDouble(scanner.nextLine().trim());
            if (quantity <= 0) return "Error: quantity must be positive.";
        } catch (NumberFormatException e) {
            return "Error: invalid number.";
        }

        try {
            RemoveOperation op = new RemoveOperation(name, quantity);
            return op.execute(storage);
        } catch (exceptions.ProductNotFoundException e) {
            return "Error: " + e.getMessage();
        } catch (exceptions.InsufficientStockException e) {
            return handleInsufficientStock(e, name);
        }
    }

    /**
     * Helper used when the user asks to remove more than is available
     * Offers to remove only the available quantity instead
     * @param e the InsufficientStockException with available info
     * @param name product name
     * @return result message or cancellation message
     */
    private String handleInsufficientStock(exceptions.InsufficientStockException e, String name) {
        double available = e.getAvailable();
        String unit = e.getUnit();

        System.out.printf("Insufficient stock for '%s'. Available batches:%n", name);
        storage.getStock().get(name).forEach(p ->
                System.out.printf("  %.2f %s  expiry: %s  loc: %s%n",
                        p.getQuantity(), p.getMeasureUnit(), p.getExpireDate(), p.getLocation()));
        System.out.printf("Total available: %.2f %s%n", available, unit);

        System.out.print("Remove all available stock? (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        if (answer.equals("yes") || answer.equals("y")) {
            RemoveOperation op = new RemoveOperation(name, available);
            return op.execute(storage);
        }
        return "Remove cancelled.";
    }
}
