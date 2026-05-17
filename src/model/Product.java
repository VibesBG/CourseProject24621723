package model;

import java.time.LocalDate;

/**
 * Product in the storage
 * Keeps name, manufacturer, unit, price, quantities,
 * entry and expiry dates, comments, and location
 */
public class Product {

    /** Product name */
    private final String name;

    /** Manufacturer name */
    private final String manufacturerName;

    /** Measure unit */
    private final MeasureUnit measureUnit;

    /** Price per single unit */
    private final double pricePerUnit;

    /** Expiration date of a product */
    private final LocalDate expireDate;

    /** Date of enter */
    private final LocalDate dateOfEnter;

    /** Quantity of the product */
    private double quantity;

    /** Optional comment or note */
    private String comment;

    /** Location (section, rack, slot) */
    private Location location;

    /**
     * Private constructor that initializes the product using a Builder object
     * @param b the Builder instance containing the field values
     */
    private Product(Builder b) {
        this.name = b.name;
        this.manufacturerName = b.manufacturerName;
        this.measureUnit = b.measureUnit;
        this.pricePerUnit = b.pricePerUnit;
        this.expireDate = b.expireDate;
        this.dateOfEnter = b.dateOfEnter;
        this.quantity = b.quantity;
        this.comment = b.comment;
    }

    /**
     * Gets the products name
     * @return the products name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the manufacturer name
     * @return the manufacturer name
     */
    public String getManufacturerName() {
        return manufacturerName;
    }

    /**
     * Gets the unit of measurement
     * @return the measure unit
     */
    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    /**
     * Gets the price per single unit
     * @return the price
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Gets the expiration date of the batch
     * @return the expiry date
     */
    public LocalDate getExpireDate() {
        return expireDate;
    }

    /**
     * Gets the date when the product entered the storage
     * @return the arrival date
     */
    public LocalDate getDateOfEnter() {
        return dateOfEnter;
    }

    /**
     * Gets the quantity of the product
     * @return the available quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Gets the comment or note
     * @return the comment string
     */
    public String getComment() {
        return comment;
    }

    /**
     * Gets the location
     * @return the Location object
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the product quantity
     * @param quantity the new quantity
     * @throws IllegalArgumentException if quantity is less than 0
     */
    public void setQuantity(double quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        this.quantity = quantity;
    }

    /**
     * Sets a comment for this product
     * @param comment the text of comment
     */
    public void setComment(String comment) {
        this.comment = comment == null ? "" : comment;
    }

    /**
     * Sets a location to this product
     * @param location the new location
     * @throws IllegalArgumentException if location is null
     */
    public void setLocation(Location location) {
        if (location == null) throw new IllegalArgumentException("Location cannot be null.");
        this.location = location;
    }

    /**
     * Checks if expiration date is before the current date
     * @return true if the product is expired and false if it's not
     */
    public boolean isExpired() {
        return expireDate.isBefore(LocalDate.now());
    }

    /**
     * Checks if the product expires within a given number of days from today
     * @param days the number of days for the check
     * @return true if it expires soon or is already expired and false if it doesn't
     */
    public boolean expiresSoon(int days) {
        return !expireDate.isAfter(LocalDate.now().plusDays(days));
    }

    /**
     * Converts the product object to string
     * @return a string representation of the product
     */
    @Override
    public String toString() {
        return String.format(
                "Product{name='%s', manufacturer='%s', unit='%s', price=%.2f, " +
                        "expiry=%s, arrival=%s, qty=%.2f}",
                name, manufacturerName, measureUnit, pricePerUnit,
                expireDate, dateOfEnter, quantity);
    }

    /**
     * Inner static class implementing the Builder Pattern to allow safe and
     * step-by-step creation of Product instances
     */
    public static class Builder {

        /** The name for the product */
        private final String name;

        /** The manufacturer name, defaults to an empty string */
        private String manufacturerName = "";

        /** The measurement unit, defaults to KG */
        private MeasureUnit measureUnit = MeasureUnit.KG;

        /** The price per unit, defaults to 0.0 */
        private double pricePerUnit = 0.0;

        /** The expiry date, defaults to 1 year from the current date */
        private LocalDate expireDate = LocalDate.now().plusYears(1);

        /** The arrival date, defaults to the current date */
        private LocalDate dateOfEnter = LocalDate.now();

        /** The products quantity, defaults to 0.0 */
        private double quantity = 0.0;

        /** Optional comment, defaults to an empty string */
        private String comment = "";

        /**
         * Constructor for the Builder, requiring a product name that is not empty
         * @param name the name of the product
         * @throws IllegalArgumentException if the name is null or blank
         */
        public Builder(String name) {
            if (name == null || name.isBlank())
                throw new IllegalArgumentException("Product name cannot be empty.");
            this.name = name;
        }

        /**
         * Sets the manufacturer name for the product
         * @param manufacturerName the name of the manufacturer
         * @return the current Builder instance
         */
        public Builder manufacturer(String manufacturerName) {
            this.manufacturerName = manufacturerName == null ? "" : manufacturerName;
            return this;
        }

        /**
         * Sets the measure unit by parsing a raw string into a MeasureUnit enum
         * @param measureUnit the string representation of the unit
         * @return the current Builder instance
         * @throws IllegalArgumentException if the unit string is null
         */
        public Builder unit(String measureUnit) {
            if (measureUnit == null)
                throw new IllegalArgumentException("Unit cannot be empty.");
            this.measureUnit = MeasureUnit.fromString(measureUnit);
            return this;
        }

        /**
         * Sets the unit price of the product
         * @param price the price value
         * @return the current Builder instance
         * @throws IllegalArgumentException if the price is negative
         */
        public Builder pricePerUnit(double price) {
            if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
            this.pricePerUnit = price;
            return this;
        }

        /**
         * Sets the expiration date of the product batch
         * @param date the expiration date
         * @return the current Builder instance
         * @throws IllegalArgumentException if the date is null
         */
        public Builder expiryDate(LocalDate date) {
            if (date == null) throw new IllegalArgumentException("Expiry date cannot be null.");
            this.expireDate = date;
            return this;
        }

        /**
         * Sets the arrival date of the product into the warehouse
         * @param date the entry date
         * @return the current Builder instance
         * @throws IllegalArgumentException if the date is null
         */
        public Builder arrivalDate(LocalDate date) {
            if (date == null) throw new IllegalArgumentException("Arrival date cannot be null.");
            this.dateOfEnter = date;
            return this;
        }

        /**
         * Sets the initial quantity of the product batch
         * @param quantity the quantity value
         * @return the current Builder instance
         * @throws IllegalArgumentException if the quantity is negative
         */
        public Builder quantity(double quantity) {
            if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
            this.quantity = quantity;
            return this;
        }

        /**
         * Sets a comment or note for the product
         * @param comment the text of the comment or note
         * @return the current Builder instance
         */
        public Builder comment(String comment) {
            this.comment = comment == null ? "" : comment;
            return this;
        }

        /**
         * Validates and constructs the final Product instance
         * @return a fully constructed Product object
         * @throws IllegalStateException if the arrival date is after the expiration date
         */
        public Product build() {
            if (dateOfEnter.isAfter(expireDate))
                throw new IllegalStateException("Arrival date cannot be after expiry date.");
            return new Product(this);
        }
    }
}
