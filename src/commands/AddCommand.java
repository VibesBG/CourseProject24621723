package commands;

import model.Location;
import model.Product;
import model.Storage;
import operations.AddOperation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AddCommand {

    private final Storage storage;
    private final Scanner scanner;

    public AddCommand(Storage storage, Scanner scanner) {
        this.storage = storage;
        this.scanner = scanner;
    }

    public String execute() {
        try {
            System.out.print("Product name: ");
            String name = scanner.nextLine().trim();
            if (name.isBlank()) return "Error: name cannot be empty.";

            System.out.print("Manufacturer: ");
            String manufacturer = scanner.nextLine().trim();

            System.out.print("Unit (kg / litres): ");
            String unit = scanner.nextLine().trim();
            if (unit.isBlank()) return "Error: unit cannot be empty.";

            System.out.print("Price per unit: ");
            double price = parsePositiveDouble(scanner.nextLine().trim(), "price");

            System.out.print("Expiry date (YYYY-MM-DD): ");
            LocalDate expiry = parseDate(scanner.nextLine().trim());

            System.out.print("Arrival date (YYYY-MM-DD): ");
            LocalDate arrival = parseDate(scanner.nextLine().trim());

            System.out.print("Quantity: ");
            double quantity = parsePositiveDouble(scanner.nextLine().trim(), "quantity");

            System.out.print("Comment (optional): ");
            String comment = scanner.nextLine().trim();

            Location location = askForLocation(name, expiry);
            if (location == null) return "Add cancelled.";

            Product product = new Product.Builder(name)
                    .manufacturer(manufacturer)
                    .unit(unit)
                    .pricePerUnit(price)
                    .expiryDate(expiry)
                    .arrivalDate(arrival)
                    .quantity(quantity)
                    .comment(comment)
                    .build();

            AddOperation op = new AddOperation(product, location);
            return op.execute(storage);

        } catch (IllegalArgumentException | IllegalStateException e) {
            return "Error: " + e.getMessage();
        }
    }

    private Location askForLocation(String name, LocalDate expiry) {
        List<Product> existing = storage.getStock().get(name);
        if (existing == null || existing.isEmpty()) return promptLocation();

        Optional<Product> sameExpiry = existing.stream()
                .filter(p -> p.getExpireDate().equals(expiry))
                .findFirst();

        if (sameExpiry.isPresent()) {
            Location loc = sameExpiry.get().getLocation();
            System.out.printf("Info: '%s' (expiry %s) already at %s.%n", name, expiry, loc);
            return loc;
        }

        System.out.printf("Warning: '%s' exists with different expiry. Choose a DIFFERENT location.%n", name);
        existing.forEach(p -> System.out.printf("  Occupied: %s (expiry: %s)%n",
                p.getLocation(), p.getExpireDate()));

        while (true) {
            Location candidate = promptLocation();
            if (candidate == null) return null;
            boolean conflict = existing.stream()
                    .anyMatch(p -> p.getLocation().equals(candidate)
                            && !p.getExpireDate().equals(expiry));
            if (conflict) System.out.println("Location taken. Choose another.");
            else return candidate;
        }
    }

    private Location promptLocation() {
        while (true) {
            System.out.print("Location (section/rack/slot e.g. A/1/3, or 'cancel'): ");
            String raw = scanner.nextLine().trim();
            if (raw.equalsIgnoreCase("cancel")) return null;
            try { return Location.fromString(raw); }
            catch (IllegalArgumentException e) {
                System.out.println("Invalid format. Use section/rack/slot.");
            }
        }
    }

    private LocalDate parseDate(String s) {
        try { return LocalDate.parse(s); }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date '" + s + "'. Use YYYY-MM-DD.");
        }
    }

    private double parsePositiveDouble(String s, String field) {
        try {
            double v = Double.parseDouble(s);
            if (v <= 0) throw new IllegalArgumentException(field + " must be positive.");
            return v;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number for " + field + ": " + s);
        }
    }
}