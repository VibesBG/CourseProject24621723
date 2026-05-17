package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class representing the main storage
 * Manages the product stock map and change log entries
 */
public class Storage {

    /**
     * Private constructor
     */
    private Storage() {
    }

    /**
     * Inner static class that holds the Storage instance
     * Implements the holder idiom for safe lazy loading
     */
    private static class Holder {
        private static final Storage INSTANCE = new Storage();
    }

    /**
     * Provides access to the single Storage
     * @return the instance of Storage
     */
    public static Storage getInstance() {
        return Holder.INSTANCE;
    }

    /** Map linking product names to lists of different batches */
    private final Map<String, List<Product>> stock = new LinkedHashMap<>();

    /** List keeping track of all operations for logging */
    private final List<LogEntry> log = new ArrayList<>();

    /**
     * Gets the entire storage inventory
     * @return the stock map
     */
    public Map<String, List<Product>> getStock() {
        return stock;
    }

    /**
     * Gets log history entries
     * @return the log entries list
     */
    public List<LogEntry> getLogEntries() {
        return log;
    }

    /**
     * Clears the stock inventory and history logs
     */
    public void clear() {
        stock.clear();
        log.clear();
    }
}