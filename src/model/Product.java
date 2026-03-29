import java.time.LocalDate;

public class Product {
    private final String name;
    private final String manufacturerName;
    private final String measureUnit;
    private final double pricePerUnit;
    private final LocalDate expireDate;
    private final LocalDate dateOfEnter;
    private double quantity;
    private String comment;
    private Location location;

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

    public void setQuantity(double quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        this.quantity = quantity;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? "" : comment;
    }

    public void setLocation(Location location) {
        if (location == null) throw new IllegalArgumentException("Location cannot be null.");
        this.location = location;
    }

    public boolean isExpired() {
        return expireDate.isBefore(LocalDate.now());
    }

    public boolean expiresSoon(int days) {
        return !expireDate.isAfter(LocalDate.now().plusDays(days));
    }

    @Override
    public String toString() {
        return String.format(
                "Product{name='%s', manufacturer='%s', unit='%s', price=%.2f, " +
                        "expiry=%s, arrival=%s, qty=%.2f}",
                name, manufacturerName, measureUnit, pricePerUnit,
                expireDate, dateOfEnter, quantity);
    }

    public static class Builder {

        private final String name;

        private String manufacturerName = "";
        private String measureUnit = "бр";
        private double pricePerUnit = 0.0;
        private LocalDate expireDate = LocalDate.now().plusYears(1);
        private LocalDate dateOfEnter = LocalDate.now();
        private double quantity = 0.0;
        private String comment = "";

        public Builder(String name) {
            if (name == null || name.isBlank())
                throw new IllegalArgumentException("Product name cannot be empty.");
            this.name = name;
        }

        public Builder manufacturer(String manufacturerName) {
            this.manufacturerName = manufacturerName == null ? "" : manufacturerName;
            return this;
        }

        public Builder unit(String measureUnit) {
            if (measureUnit == null || measureUnit.isBlank())
                throw new IllegalArgumentException("Unit cannot be empty.");
            this.measureUnit = measureUnit;
            return this;
        }

        public Builder pricePerUnit(double price) {
            if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
            this.pricePerUnit = price;
            return this;
        }

        public Builder expiryDate(LocalDate date) {
            if (date == null) throw new IllegalArgumentException("Expiry date cannot be null.");
            this.expireDate = date;
            return this;
        }

        public Builder arrivalDate(LocalDate date) {
            if (date == null) throw new IllegalArgumentException("Arrival date cannot be null.");
            this.dateOfEnter = date;
            return this;
        }

        public Builder quantity(double quantity) {
            if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
            this.quantity = quantity;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment == null ? "" : comment;
            return this;
        }

        public Product build() {
            if (dateOfEnter.isAfter(expireDate))
                throw new IllegalStateException("Arrival date cannot be after expiry date.");
            return new Product(this);
        }
    }
}
