package operations;

import model.LogEntry;
import model.Storage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Operation that prints all log entries between two dates inclusive
 * Entries are sorted by date
 */
public class LogOperation implements StorageOperation {

    /** Start of the date range */
    private final LocalDate from;

    /** End of the date range */
    private final LocalDate to;

    /**
     * Constructor
     * @param from start date inclusive
     * @param to end date inclusive
     */
    public LogOperation(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Filters and prints the log entries in the given range
     * @param storage the storage to read from
     * @return formatted log or a message that nothing was found
     */
    @Override
    public String execute(Storage storage) {
        List<LogEntry> filtered = storage.getLogEntries().stream()
                .filter(e -> !e.getDate().isBefore(from) && !e.getDate().isAfter(to))
                .sorted(Comparator.comparing(LogEntry::getDate))
                .toList();

        if (filtered.isEmpty())
            return "No changes recorded between " + from + " and " + to + ".";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Log from %s to %s:%n", from, to));
        sb.append("-".repeat(90)).append("\n");
        filtered.forEach(e -> sb.append(e).append("\n"));
        return sb.toString();
    }
}
