package operations;

import model.Storage;

/**
 * Common contract for all operations that act on the Storage
 * Implementations contain the actual business logic (add, remove, clean, log, print)
 */
public interface StorageOperation {

    /**
     * Executes the operation against the given storage
     * @param storage the storage to operate on
     * @return a string describing the result
     */
    String execute(Storage storage);
}
