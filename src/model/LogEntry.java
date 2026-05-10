package model;

import java.time.LocalDate;

public class LogEntry {

    public enum Type {ADD, REMOVE, CLEAN}

    private final LocalDate date;
    private final Type type;
    private final String productName;
    private final double quantity;
    private final String unit;
    private final Location location;
    private final LocalDate expiryDate;
    private final String note;

    public LogEntry(LocalDate date, Type type, String productName,
                    double quantity, String unit,
                    Location location, LocalDate expiryDate, String note) {
        this.date = date;
        this.type = type;
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.location = location;
        this.expiryDate = expiryDate;
        this.note = note == null ? "" : note;
    }

    public LocalDate getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }

    public String getProductName() {
        return productName;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-6s | %-20s | %7.2f %-7s | expiry: %s | loc: %s%s",
                date, type, productName, quantity, unit, expiryDate, location,
                note.isBlank() ? "" : " | " + note);
    }
}