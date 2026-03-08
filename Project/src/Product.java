public class Product {
    private String name;
    private String expireDate;
    private String dateOfEnter;
    private String manufacturerName;
    private String measureUnit;
    private double quantity;
    private String comment;

    public Product(String name, String expireDate, String dateOfEnter, String manufacturerName, String measureUnit, double quantity, String comment) {
        this.name = name;
        this.expireDate = expireDate;
        this.dateOfEnter = dateOfEnter;
        this.manufacturerName = manufacturerName;
        this.measureUnit = measureUnit;
        this.quantity = quantity;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getDateOfEnter() {
        return dateOfEnter;
    }

    public void setDateOfEnter(String dateOfEnter) {
        this.dateOfEnter = dateOfEnter;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
