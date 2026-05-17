package commands;

/**
 * Common contract for all user commands
 * Each command takes its arguments and returns a result message
 */
public interface Command {

    /**
     * Executes the command with the given arguments
     * @param args the arguments typed after the command name
     * @return a message describing what happened
     */
    String execute(String[] args);
}
