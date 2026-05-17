import commands.CommandParser;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandParser parser  = new CommandParser(scanner);

        System.out.println("=== Storage Management System ===");
        System.out.println("Type 'help' for a list of commands.");
        System.out.println();

        if (args.length > 0) {
            parser.parse("open " + args[0]);
        }

        while (parser.isRunning()) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            parser.parse(scanner.nextLine());
        }

        scanner.close();
    }
}