package commands;

import model.Storage;
import operations.LogOperation;
import validators.DateValidator;

import java.time.LocalDate;

/**
 * Command that prints the log of all changes between two dates
 */
public class LogCommand implements Command {

    /** The storage to read the log from */
    private final Storage storage;

    /**
     * Constructor
     * @param storage the storage to read from
     */
    public LogCommand(Storage storage) {
        this.storage = storage;
    }

    /**
     * Parses the date range arguments and runs a LogOperation
     * @param args expects two dates in YYYY-MM-DD format
     * @return the formatted log or an error message
     */
    @Override
    public String execute(String[] args) {
        if (args.length < 2)
            return "Usage: log <from> <to>  (YYYY-MM-DD)";
        try {
            LocalDate from = DateValidator.parse(args[0]);
            LocalDate to = DateValidator.parse(args[1]);
            DateValidator.validateRange(from, to);
            LogOperation op = new LogOperation(from, to);
            return op.execute(storage);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }
}
