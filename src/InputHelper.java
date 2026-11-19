import java.io.Console;
import java.util.Scanner;

public class InputHelper {

    private final Scanner scanner;
    private final Console console;
    private boolean warningShown = false;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
        this.console = System.console();
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        System.out.flush();

        if (!scanner.hasNextLine()) {
            return null;
        }

        return scanner.nextLine();
    }

    public char[] readPassword(String prompt) {
        if (console != null) {
            return console.readPassword("%s", prompt);
        }

        if (!warningShown) {
            System.out.println("WARNING: No secure console found. "
                    + "Your password will be VISIBLE while you type.");
            warningShown = true;
        }

        System.out.print(prompt);
        System.out.flush();

        if (!scanner.hasNextLine()) {
            return null;
        }

        return scanner.nextLine().toCharArray();
    }
}
