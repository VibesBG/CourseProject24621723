package commands;

import model.Storage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Parses lines typed by the user and dispatches them to the right Command
 * Also manages the file lifecycle (open, close, save, save as, exit)
 */
public class CommandParser {

    /** The storage shared between all commands */
    private final Storage storage;

    /** Scanner used to read user input */
    private final Scanner scanner;

    /** Keeps track of the file currently opened */
    private final FileSession session = new FileSession();

    /** Flag if the program loop should keep running */
    private boolean running = true;

    /**
     * Constructor
     * @param scanner the scanner from which command lines are read
     */
    public CommandParser(Scanner scanner) {
        this.storage = Storage.getInstance();
        this.scanner = scanner;
    }

    /**
     * Checks if the program should continue running
     * @return true while the loop should keep going
     */
    public boolean isRunning() { return running; }

    /**
     * Parses one input line and runs the matching command
     * @param input the raw line typed by the user
     */
    public void parse(String input) {
        if (input == null || input.isBlank()) return;

        String trimmed = input.trim();

        if (trimmed.toLowerCase().startsWith("save as ")) {
            handleSaveAs(trimmed.substring(8).trim());
            return;
        }

        String[] tokens = trimmed.split("\\s+");
        String cmd = tokens[0].toLowerCase();
        String[] args = java.util.Arrays.copyOfRange(tokens, 1, tokens.length);

        switch (cmd) {
            case "open" -> handleOpen(tokens.length > 1 ? tokens[1].trim() : "");
            case "close" -> handleClose();
            case "save" -> handleSave();
            case "help" -> System.out.println(new HelpCommand().execute(args));
            case "exit" -> handleExit();
            case "print" -> runIfFileOpen(new PrintCommand(storage), args, false);
            case "add" -> runIfFileOpen(new AddCommand(storage, scanner), args, true);
            case "remove" -> runIfFileOpen(new RemoveCommand(storage, scanner), args, true);
            case "log" -> runIfFileOpen(new LogCommand(storage), args, false);
            case "clean" -> runIfFileOpen(new CleanCommand(storage, scanner), args, true);
            default -> System.out.println("Unknown command '" + cmd + "'. Type 'help'.");
        }
    }

    /**
     * Runs a Command if a file is currently open
     * @param command the command to run
     * @param args the arguments to pass to the command
     * @param mutates true if the command changes the data (sets the unsaved flag)
     */
    private void runIfFileOpen(Command command, String[] args, boolean mutates) {
        if (!session.isOpen()) {
            System.out.println("No file open. Use 'open <filepath>' first.");
            return;
        }
        System.out.println(command.execute(args));
        if (mutates) session.markChanged();
    }

    /**
     * Handles the 'open' command
     * @param path path of the file to open
     */
    private void handleOpen(String path) {
        if (path.isBlank()) { System.out.println("Usage: open <filepath>"); return; }
        if (session.isOpen() && session.hasUnsavedChanges() && !confirmDiscard()) return;
        try {
            FileManager.load(storage, path);
            session.open(path);
            System.out.println("Successfully opened " + new File(path).getName());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
            running = false;
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }

    /**
     * Handles the 'close' command
     */
    private void handleClose() {
        if (!session.isOpen()) { System.out.println("No file is currently open."); return; }
        if (session.hasUnsavedChanges() && !confirmDiscard()) return;
        String name = new File(session.getPath()).getName();
        storage.clear();
        session.close();
        System.out.println("Successfully closed " + name);
    }

    /**
     * Handles the 'save' command, writing back to the same file
     */
    private void handleSave() {
        if (!session.isOpen()) { System.out.println("No file open. Use 'save as <filepath>'."); return; }
        try {
            FileManager.save(storage, session.getPath());
            session.markSaved();
            System.out.println("Successfully saved " + new File(session.getPath()).getName());
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    /**
     * Handles the 'save as' command, writing to a new file
     * @param path the new file path
     */
    private void handleSaveAs(String path) {
        if (path.isBlank()) { System.out.println("Usage: save as <filepath>"); return; }
        try {
            FileManager.save(storage, path);
            session.open(path);
            System.out.println("Successfully saved " + new File(path).getName());
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    /**
     * Handles the 'exit' command, prompting to save if there are unsaved changes
     */
    private void handleExit() {
        if (session.isOpen() && session.hasUnsavedChanges()) {
            System.out.print("You have unsaved changes. Save before exiting? (yes/no): ");
            String ans = scanner.nextLine().trim().toLowerCase();
            if (ans.equals("yes") || ans.equals("y")) handleSave();
        }
        System.out.println("Exiting the program...");
        running = false;
    }

    /**
     * Asks the user whether to discard unsaved changes
     * @return true if the user agreed to discard
     */
    private boolean confirmDiscard() {
        System.out.print("You have unsaved changes. Discard? (yes/no): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        return ans.equals("yes") || ans.equals("y");
    }
}
