package commands;

/**
 * Keeps track of the currently opened file
 * Knows the file path, whether a file is open and if there are unsaved changes
 */
public class FileSession {

    /**
     * Default constructor
     */
    public FileSession() {
    }

    /** Path of the file that is currently opened */
    private String currentFilePath = null;

    /** Flag if a file is currently loaded */
    private boolean fileOpen = false;

    /** Flag if there are unsaved changes in memory */
    private boolean unsavedChanges = false;

    /**
     * Marks the session as open with the given file path
     * @param path path of the opened file
     */
    public void open(String path) {
        this.currentFilePath = path;
        this.fileOpen = true;
        this.unsavedChanges = false;
    }

    /**
     * Marks the session as closed and clears the path
     */
    public void close() {
        this.currentFilePath = null;
        this.fileOpen = false;
        this.unsavedChanges = false;
    }

    /**
     * Marks that there are unsaved changes
     */
    public void markChanged() {
        this.unsavedChanges = true;
    }

    /**
     * Marks the current state as saved
     */
    public void markSaved() {
        this.unsavedChanges = false;
    }

    /**
     * Checks if a file is currently open
     * @return true if a file is loaded
     */
    public boolean isOpen() {
        return fileOpen;
    }

    /**
     * Checks if there are unsaved changes
     * @return true if there are unsaved changes
     */
    public boolean hasUnsavedChanges() {
        return unsavedChanges;
    }

    /**
     * Gets the path of the currently opened file
     * @return file path or null if no file is open
     */
    public String getPath() {
        return currentFilePath;
    }
}
