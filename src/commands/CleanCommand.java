package commands;

import model.Storage;
import operations.CleanOperation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CleanCommand {

    private final Storage storage;
    private final Scanner scanner;

    public CleanCommand(Storage storage, Scanner scanner) {
        this.storage = storage;
        this.scanner = scanner;
    }

    public String execute(String[] args) {
        int days = 0;
        if (args.length >= 1) {
            try {
                days = Integer.parseInt(args[0]);
                if (days < 0) return "Error: days cannot be negative.";
            } catch (NumberFormatException e) {
                return "Error: invalid number of days.";
            }
        }

        System.out.print("Calculate losses for a product? (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (!answer.equals("yes") && !answer.equals("y")) {
            CleanOperation op = new CleanOperation(days);
            return op.execute(storage);
        }

        System.out.print("Product name: ");
        String lossProduct = scanner.nextLine().trim();

        System.out.print("Price per unit: ");
        double lossPrice;
        try {
            lossPrice = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return "Error: invalid price.";
        }

        System.out.print("Loss period FROM (YYYY-MM-DD): ");
        LocalDate lossFrom;
        try { lossFrom = LocalDate.parse(scanner.nextLine().trim()); }
        catch (DateTimeParseException e) { return "Error: invalid date."; }

        System.out.print("Loss period TO (YYYY-MM-DD): ");
        LocalDate lossTo;
        try { lossTo = LocalDate.parse(scanner.nextLine().trim()); }
        catch (DateTimeParseException e) { return "Error: invalid date."; }

        CleanOperation op = new CleanOperation(days, lossProduct, lossPrice, lossFrom, lossTo);
        return op.execute(storage);
    }
}