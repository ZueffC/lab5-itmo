package itmo.lab5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.*;

import itmo.lab5.cli.CommandBuilder;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.cli.CommandRegistry;
import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.commands.*;
import itmo.lab5.models.Flat;
import itmo.lab5.parser.Reader;
import itmo.lab5.cli.helpers.History;

public class App {
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());

    public static void main(String[] g_args) {
        Path dataFilePath = null;
        var history = new History();
        var flats = new HashMap<Integer, Flat>();

        CommandContext context = new CommandContext();
        CommandRegistry registry = new CommandBuilder()
                .register("exit", new ExitCommand())
                .register("help", new HelpCommand())
                .register("info", new InfoCommand())
                .register("clear", new ClearCommand())
                .register("show", new ShowCommand())
                .register("remove_key", new RemoveKeyCommand())
                .register("history", new HistoryCommand())
                .register("insert", new InsertCommand())
                .register("update", new UpdateCommand())
                .register("save", new SaveCommand())
                .build();

        try {
            dataFilePath = getDataFileFromEnv("LAB5_DATA");
            flats = new Reader().parseCSV(dataFilePath.toFile());
        } catch (IllegalArgumentException | IOException e) {
            LOGGER.log(Level.WARNING, "There's error while loading file: " + e.getMessage());
            return;
        }

        context.set("registry", registry);
        context.set("collection", flats);
        context.set("history", history);
        context.set("path", dataFilePath.toString());

        var scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");

            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                continue;

            String[] args = input.split(" ");
            String commandName = args[0];
            Command command = registry.getByName(commandName);

            if (command != null) {
                history.add(commandName);
                var localArgs = args.length > 1 ? java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];
                var response = command.execute(localArgs, context);
                System.out.println(response);
            } else {
                System.out.println("Unknown command: " + commandName);
            }
        }

    }

    public static Path getDataFileFromEnv(String envVariable) throws IOException {
        String envPath = System.getenv(envVariable);
        final Path path;

        if (envPath == null || envPath.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Environment variable '" + envVariable + "' is not set or empty.");
        }

        try {
            path = Paths.get(envPath);
        } catch (InvalidPathException ex) {
            throw new IllegalArgumentException(
                    "The path provided in environment variable '" +
                            envVariable + "' is invalid: " + ex.getMessage(),
                    ex);
        }

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("The file at path '" + path + "' does not exist.");
        }

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("The path '" + path + "' is not a file. Check twice!");
        }

        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("The file at path '" + path + "' is not readable. " +
                    "Check file permissions!");
        }

        LOGGER.info("File '" + path + "' exists and is readable.");
        return path;
    }
}
