package commands;

import model.Storage;
import operations.PrintOperation;

/**
 * Command wrapper around PrintOperation
 * Lists all available products in the warehouse
 */
public class PrintCommand implements Command {

    /** The storage to read from */
    private final Storage storage;

    /**
     * Constructor
     * @param storage the storage to print
     */
    public PrintCommand(Storage storage) {
        this.storage = storage;
    }

    /**
     * Runs a PrintOperation and returns its output
     * @param args ignored
     * @return formatted table of products
     */
    @Override
    public String execute(String[] args) {
        PrintOperation op = new PrintOperation();
        return op.execute(storage);
    }
}
