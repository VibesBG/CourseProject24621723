package commands;

/**
 * Command that prints a short list of all supported commands
 */
public class HelpCommand implements Command {

    /**
     * Default constructor
     */
    public HelpCommand() {
    }

    /**
     * Builds and returns the help text
     * @param args ignored
     * @return formatted help string with all commands
     */
    @Override
    public String execute(String[] args) {
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
