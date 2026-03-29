import java.util.ArrayList;
import java.util.List;

public class Storage {
    private List<Product> products;

    public Storage(List<Product> products) {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }
}
