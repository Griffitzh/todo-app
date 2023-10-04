import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Put your file in the assets folder and refer to it as using an absolute path:
    private static Path filePath = Paths.get(
            "C:\\vscode_projects\\greenfox\\to-do\\to-do\\assets\\todo.txt");
    private static List<String> lines = null;

    public static void main(String[] args) {
        if (args.length < 1) {
            String tasks =
                    "Command Line Todo application\n" +
                            "=============================\n" +
                            "\n" +
                            "Command line arguments:\n" +
                            "    -l   Lists all the tasks\n" +
                            "    -a   Adds a new task\n" +
                            "    -r   Removes an task\n" +
                            "    -c   Completes an task";
            System.out.println(tasks);

        } else if (args[0].equals("-l")) {
            readFile();

            boolean completed = false;

            int lineCounter = 1;
            if (lines.isEmpty()) {
                System.out.println("No todos for today! :)");
            } else {
                for (String line : lines) {
                    if (line.contains("isCompleted")) {
                        completed = true;
                    } else {
                        completed = false;
                    }
                    System.out.print(lineCounter++ + " - " + (completed ? "[x] " : "[ ] "));
                    String listItem = line.contains("isCompleted") ? line.substring(0, line.length() - 11) : line;
                    System.out.println(listItem);
                }
            }

        } else if (args[0].equals("-a")) {
            if (args.length < 2) {
                System.out.println("Unable to add: no task provided");
                System.exit(1);
            }
            lines = new ArrayList<>();
            for (int i = 1; i < args.length; i++) {
                lines.add(args[i]);
            }
            try {
                Files.write(filePath, lines, StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println("Couldn't write file");
                System.exit(1);
            }

        } else if (args[0].equals("-r")) {
            if (args.length < 2) {
                System.out.println("Unable to remove: no index provided");
                System.exit(1);
            }
            int indexForDelete = 0;
            try {
                indexForDelete = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Unable to remove: index is not a number");
                System.exit(1);
            }
            readFile();

            if (lines.size() < indexForDelete) {
                System.out.println("Unable to remove: index is out of bound");
                System.exit(1);
            }
            lines.remove(indexForDelete - 1);

            writeFile();

        } else if (args[0].equals("-c")) {
            System.out.println("Here you should complete a task");

            if (args.length < 2) {
                System.out.println("Unable to complete: no index provided");
                System.exit(1);
            }
            int indexForComplete = 0;
            try {
                indexForComplete = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Unable to complete: index is not a number");
                System.exit(1);
            }

            readFile();

            if (lines.size() < indexForComplete) {
                System.out.println("Unable to complete: index is out of bound");
                System.exit(1);
            }

            StringBuilder stringBuilder;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.indexOf(lines.get(i)) == indexForComplete - 1) {
                    stringBuilder = new StringBuilder(lines.get(i));
                    stringBuilder.append("isCompleted");
                    lines.set(lines.indexOf(lines.get(i)), stringBuilder.toString());
                }
            }
            writeFile();
        }
    }

    public static List<String> readFile() {
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Couldn't read the file");
            System.exit(1);
        }
        return lines;
    }

    public static void writeFile() {
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Couldn't write file");
            System.exit(1);
        }
    }
}