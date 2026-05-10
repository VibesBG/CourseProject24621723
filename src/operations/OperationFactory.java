package operations;

import model.Location;
import model.Product;

import java.time.LocalDate;

public class OperationFactory {

    private OperationFactory() {}

    public static StorageOperation print() {
        return new PrintOperation();
    }

    public static StorageOperation add(Product product, Location location) {
        return new AddOperation(product, location);
    }

    public static StorageOperation remove(String productName, double quantity) {
        return new RemoveOperation(productName, quantity);
    }

    public static StorageOperation log(LocalDate from, LocalDate to) {
        return new LogOperation(from, to);
    }

    public static StorageOperation clean(int daysThreshold) {
        return new CleanOperation(daysThreshold);
    }

    public static StorageOperation clean(int daysThreshold,
                                         String lossProduct, double lossPrice,
                                         LocalDate lossFrom, LocalDate lossTo) {
        return new CleanOperation(daysThreshold, lossProduct, lossPrice, lossFrom, lossTo);
    }
}