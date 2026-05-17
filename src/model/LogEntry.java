package model;

import java.time.LocalDate;

/**
 * Represents a single history log entry for warehouse changes
 * Tracks operations like adding, removing, or cleaning products
 */
public class LogEntry {

    /** Types of operations */
    public enum Type {ADD, REMOVE, CLEAN}

    /** Date of log */
    private final LocalDate date;

    /** Type of the action */
    private final Type type;

    /** Name of the logged product */
    private final String productName;

    /** Amount logged during the action */
    private final double quantity;

    /** Unit of measurement of log */
    private final MeasureUnit unit;

    /** Location of the product to log */
    private final Location location;

    /** Expiration date of the batch */
    private final LocalDate expiryDate;

    /** Comment or note for the log */
    private final String note;

    /**
     * Complete constructor to create a log entry
     * @param date date
     * @param type type enum
     * @param productName product name
     * @param quantity amount
     * @param unit measure unit
     * @param location storage location
     * @param expiryDate product batch expiration
     * @param note note or comments
     */
    public LogEntry(LocalDate date, Type type, String productName,
                    double quantity, MeasureUnit unit,
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

    /**
     * Gets log date
     * @return LocalDate log date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets log type
     * @return Type
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets product name
     * @return product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Gets quantity
     * @return quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Gets measurement unit
     * @return MeasureUnit
     */
    public MeasureUnit getUnit() { return unit; }

    /**
     * Gets storage location
     * @return Location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets expiration date
     * @return LocalDate
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    /**
     * Gets note
     * @return note comment
     */
    public String getNote() {
        return note;
    }

    /**
     * Formats the log entry for nice console presentation and how it will be saved to a file
     * @return formatted log string
     */
    @Override
    public String toString() {
        return String.format("[%s] %-6s | %-20s | %7.2f %-7s | expiry: %s | loc: %s%s",
                date, type, productName, quantity, unit, expiryDate, location,
                note.isBlank() ? "" : " | " + note);
    }
}