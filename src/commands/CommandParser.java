package commands;

import model.Storage;
import operations.PrintOperation;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CommandParser {

    private final Storage storage;
    private final Scanner scanner;
    private String  currentFilePath = null;
    private boolean fileOpen = false;
    private boolean unsavedChanges = false;
    private boolean running = true;

    public CommandParser(Scanner scanner) {
        this.storage = Storage.getInstance();
        this.scanner = scanner;
    }

    public boolean isRunning() { return running; }

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
            case "help" -> System.out.println(helpText());
            case "exit" -> handleExit();
            case "print" -> requireFile(() -> {
                PrintOperation op = new PrintOperation();
                System.out.println(op.execute(storage));
            });
            case "add" -> requireFile(() -> {
                AddCommand cmd2 = new AddCommand(storage, scanner);
                System.out.println(cmd2.execute());
                unsavedChanges = true;
            });
            case "remove" -> requireFile(() -> {
                RemoveCommand cmd2 = new RemoveCommand(storage, scanner);
                System.out.println(cmd2.execute());
                unsavedChanges = true;
            });
            case "log" -> requireFile(() -> {
                LogCommand cmd2 = new LogCommand(storage);
                System.out.println(cmd2.execute(args));
            });
            case "clean" -> requireFile(() -> {
                CleanCommand cmd2 = new CleanCommand(storage, scanner);
                System.out.println(cmd2.execute(args));
                unsavedChanges = true;
            });
            default -> System.out.println("Unknown command '" + cmd + "'. Type 'help'.");
        }
    }

    private void handleOpen(String path) {
        if (path.isBlank()) { System.out.println("Usage: open <filepath>"); return; }
        if (fileOpen && unsavedChanges && !confirmDiscard()) return;
        try {
            FileManager.load(storage, path);
            currentFilePath = path;
            fileOpen = true;
            unsavedChanges = false;
            System.out.println("Successfully opened " + new File(path).getName());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
            running = false;
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }

    private void handleClose() {
        if (!fileOpen) { System.out.println("No file is currently open."); return; }
        if (unsavedChanges && !confirmDiscard()) return;
        String name = new File(currentFilePath).getName();
        storage.clear();
        currentFilePath = null;
        fileOpen = false;
        unsavedChanges = false;
        System.out.println("Successfully closed " + name);
    }

    private void handleSave() {
        if (!fileOpen) { System.out.println("No file open. Use 'save as <filepath>'."); return; }
        try {
            FileManager.save(storage, currentFilePath);
            unsavedChanges = false;
            System.out.println("Successfully saved " + new File(currentFilePath).getName());
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    private void handleSaveAs(String path) {
        if (path.isBlank()) { System.out.println("Usage: save as <filepath>"); return; }
        try {
            FileManager.save(storage, path);
            currentFilePath = path;
            fileOpen = true;
            unsavedChanges = false;
            System.out.println("Successfully saved " + new File(path).getName());
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    private void handleExit() {
        if (fileOpen && unsavedChanges) {
            System.out.print("You have unsaved changes. Save before exiting? (yes/no): ");
            String ans = scanner.nextLine().trim().toLowerCase();
            if (ans.equals("yes") || ans.equals("y")) handleSave();
        }
        System.out.println("Exiting the program...");
        running = false;
    }

    private void requireFile(Runnable action) {
        if (!fileOpen) {
            System.out.println("No file open. Use 'open <filepath>' first.");
            return;
        }
        action.run();
    }

    private boolean confirmDiscard() {
        System.out.print("You have unsaved changes. Discard? (yes/no): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        return ans.equals("yes") || ans.equals("y");
    }

    private String helpText() {
        return """
                The following commands are supported:
                  open <file>        - opens <file>
                  close              - closes currently opened file
                  save               - saves the currently open file
                  save as <file>     - saves the currently open file in <file>
                  print              - list all products in the warehouse
                  add                - add a product (interactive)
                  remove             - remove a quantity of a product
                  log <from> <to>    - show changes between two dates (YYYY-MM-DD)
                  clean [days]       - remove expired/expiring products
                  help               - prints this information
                  exit               - exits the program
                """;
    }
}