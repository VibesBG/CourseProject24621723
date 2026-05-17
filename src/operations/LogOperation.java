package operations;

import model.LogEntry;
import model.Storage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class LogOperation implements StorageOperation {

    private final LocalDate from;
    private final LocalDate to;

    public LogOperation(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

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