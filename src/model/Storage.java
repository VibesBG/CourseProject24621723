import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Storage {

    private Storage() {
    }

    private static class Holder {
        private static final Storage INSTANCE = new Storage();
    }

    public static Storage getInstance() {
        return Holder.INSTANCE;
    }

    private final Map<String, List<Product>> stock = new LinkedHashMap<>();
    private final List<LogEntry> log = new ArrayList<>();

    public String execute(StorageOperation operation) {
        return operation.execute(stock, log);
    }

    public Map<String, List<Product>> getStock() {
        return stock;
    }

    public List<LogEntry> getLogEntries() {
        return log;
    }

    public void clear() {
        stock.clear();
        log.clear();
    }
}