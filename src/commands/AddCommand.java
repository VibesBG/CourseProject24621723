package commands;

import model.Location;
import model.Product;
import model.Storage;
import operations.AddOperation;
import validators.ProductValidator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interactive command that adds a new product into the storage
 * Reads all product fields from the user step by step and validates them
 */
public class AddCommand implements Command {

    /** The storage in which the new product is added */
    private final Storage storage;

    /** Scanner used to read user input */
    private final Scanner scanner;

    /**
     * Constructor
     * @param storage the storage to add into
     * @param scanner the scanner for user input
     */
    public AddCommand(Storage storage, Scanner scanner) {
        this.storage = storage;
        this.scanner = scanner;
    }

    /**
     * Runs the interactive add command
     * @param args ignored, input is read from the scanner
     * @return result message describing what happened
     */
    @Override
    public String execute(String[] args) {
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

            ProductValidator.validate(name, unit, price, quantity, arrival, expiry);

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

    /**
     * Asks the user for a location for a product, handling collisions with existing batches
     * If a batch with the same name and expiry already exists, its location is reused
     * Otherwise keeps prompting until the user picks a free location
     * A location is considered free only if no other product (of any name) is already there
     * @param name product name
     * @param expiry expiry date of the new batch
     * @return the chosen Location, or null if the user cancelled
     */
    private Location askForLocation(String name, LocalDate expiry) {
        List<Product> existing = storage.getStock().get(name);

        if (existing != null) {
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
        }

        while (true) {
            Location candidate = promptLocation();
            if (candidate == null) return null;
            if (isLocationFree(candidate)) return candidate;
            System.out.println("Location " + candidate + " is already occupied. Choose another.");
        }
    }

    /**
     * Checks if no existing product occupies the given location
     * @param location the location to check
     * @return true if the location is free
     */
    private boolean isLocationFree(Location location) {
        return storage.getStock().values().stream()
                .flatMap(List::stream)
                .noneMatch(p -> p.getLocation().equals(location));
    }

    /**
     * Prompts the user for a location string and parses it
     * @return parsed Location or null if the user typed 'cancel'
     */
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

    /**
     * Parses a date in YYYY-MM-DD format
     * @param s string to parse
     * @return parsed LocalDate
     * @throws IllegalArgumentException if the string is not a valid date
     */
    private LocalDate parseDate(String s) {
        try { return LocalDate.parse(s); }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date '" + s + "'. Use YYYY-MM-DD.");
        }
    }

    /**
     * Parses a positive double from a string
     * @param s the string to parse
     * @param field name of the field for the error message
     * @return parsed positive double
     * @throws IllegalArgumentException if the value is not a number or not positive
     */
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
