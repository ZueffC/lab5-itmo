package itmo.lab5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.*;

import itmo.lab5.cli.CommandBuilder;
import itmo.lab5.cli.CommandInvoker;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.cli.CommandRegistry;
import itmo.lab5.cli.commands.*;
import itmo.lab5.models.Flat;
import itmo.lab5.parser.Reader;
import itmo.lab5.cli.helpers.History;

/**
 * This class is an entry point of the application that manages a collection of
 * {@code Flat} objects.
 * The main method creates REPL that provide ability create or modificate data
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());

    /**
     * The main method that serves as the entry point for the application.
     * 
     * @param g_args command-line arguments (not used)
     */
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
                .register("save", new SaveCommand())
                .register("execute_script", new ExecuteCommand())
                .register("update", new UpdateCommand())
                .register("print_field_ascending_number_of_rooms", new FieldCommand())
                .register("filter_less_than_view", new FilterCommand("less"))
                .register("filter_greater_than_view", new FilterCommand("greater"))
                .register("replace_if_lower", new ReplaceCommand("lower"))
                .register("replace_if_greater", new ReplaceCommand("greater"))
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

        CommandInvoker invoker = new CommandInvoker(registry, context, history);
        context.set("commandInvoker", invoker);

        var scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty())
                continue;

            String[] args = input.split(" ");
            String commandName = args[0];
            String[] commandArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];

            String response = invoker.executeCommand(commandName, commandArgs);
            System.out.println(response);
        }
    }

    /**
     * Retrieves the path to the data file from the specified environment variable.
     *
     * @param envVariable the name of the environment variable that contains the
     *                    file path
     * @return the path to the data file
     * @throws IOException              if an I/O error happens
     * @throws IllegalArgumentException if the env variable is not set or invalid
     */
    public static Path getDataFileFromEnv(String envVariable) throws IOException {
        String envPath = System.getenv(envVariable);
        final Path path;

        if (envPath == null || envPath.trim().isEmpty())
            throw new IllegalArgumentException(
                    "Environment variable '" + envVariable + "' is not set or empty.");

        try {
            path = Paths.get(envPath);
        } catch (InvalidPathException ex) {
            throw new IllegalArgumentException(
                    "The path provided in environment variable '" +
                            envVariable + "' is invalid: " + ex.getMessage(),
                    ex);
        }

        if (!Files.exists(path))
            throw new IllegalArgumentException("The file at path '" + path + "' does not exist.");

        if (!Files.isRegularFile(path))
            throw new IllegalArgumentException("The path '" + path + "' is not a file. Check twice!");

        if (!Files.isReadable(path))
            throw new IllegalArgumentException("The file at path '" + path + "' is not readable. " +
                    "Check file permissions!");

        LOGGER.info("File '" + path + "' exists and is readable.");
        return path;
    }
}
