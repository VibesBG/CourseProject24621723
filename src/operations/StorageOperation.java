public interface StorageOperation {
    String execute(Map<String, List<Product>> stock, List<LogEntry> log);
}