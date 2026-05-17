package commands;

import model.Storage;
import operations.RemoveOperation;

import java.util.Scanner;

public class RemoveCommand {

    private final Storage storage;
    private final Scanner scanner;

    public RemoveCommand(Storage storage, Scanner scanner) {
        this.storage = storage;
        this.scanner = scanner;
    }

    public String execute() {
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

    private String handleInsufficientStock(exceptions.InsufficientStockException e, String name) {
        double available = e.getAvailable();
        String unit = e.getUnit();

        System.out.printf("Only %.2f %s of '%s' available.%n", available, unit, name);

        System.out.print("Remove all available stock? (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        if (answer.equals("yes") || answer.equals("y")) {
            RemoveOperation op = new RemoveOperation(name, available);
            return op.execute(storage);
        }
        return "Remove cancelled.";
    }
}