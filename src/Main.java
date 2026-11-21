import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // One Scanner shared by the whole program.
        Scanner scanner = new Scanner(System.in);

        InputHelper input = new InputHelper(scanner);

        StudentManagementSystem system =
                new StudentManagementSystem(scanner);

        StartupMenu startupMenu = new StartupMenu(system, input);

        startupMenu.run();

        scanner.close();
    }
}
