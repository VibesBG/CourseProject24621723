package commands;

import model.Storage;
import operations.LogOperation;
import validators.DateValidator;

import java.time.LocalDate;

public class LogCommand {

    private final Storage storage;

    public LogCommand(Storage storage) {
        this.storage = storage;
    }

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