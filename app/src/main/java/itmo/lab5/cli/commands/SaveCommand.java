package itmo.lab5.cli.commands;

import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.*;
import itmo.lab5.models.*;
import itmo.lab5.parser.Writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This class implements the Command interface and provides
 * functionality to save the current collection of Flat objects to a specified
 * file.
 * 
 * When executed, this command retrieves the collection and the file path from
 * the command context and uses a Writer instance to write the collection to the
 * specified file. If the collection or file path cannot be retrieved, an
 * appropriate error message is returned.
 */
public class SaveCommand implements Command {
    private static final String description = "command allows to save collection to the persistant storage";

    public final String toString() {
        return this.description;
    }

    /**
     * Executes the save command, saving the current collection of flats to a
     * file.
     *
     * @param args    an array of arguments passed to the command (not used in this
     *                command)
     * @param context the command context that contains the collection of flats and
     *                the file path
     * @return a message indicating the result of the save operation, or an error
     *         message if the collection or file path cannot be parsed
     */
    @Override
    public String execute(String[] args, CommandContext context) {
        HashMap<Integer, Flat> collection;
        String filePath;

        try {
            // Получаем коллекцию и путь к файлу из контекста
            collection = (HashMap<Integer, Flat>) context.get("collection");
            filePath = (String) context.get("path");

        } catch (ClassCastException e) {
            return "Error: Collection or file path could not be retrieved from context.";
        }

        if (filePath == null || filePath.isBlank()) {
            return "Error: File path is not defined.";
        }

        Path path = Paths.get(filePath);

        try {
            checkWritePermissions(path);
            new Writer().writeCollection(path.toString(), collection);
            return "Collection has been written!";
        } catch (SecurityException | IllegalArgumentException e) {
            return "Access denied: " + e.getMessage();
        } catch (IOException e) {
            return "An I/O error occurred while saving the collection: " + e.getMessage();
        }
    }

    /**
     * Checks whether the application can write to the given file.
     *
     * @param path the file path to check
     * @throws SecurityException if access is denied
     * @throws IOException       if an I/O error occurs
     */
    private void checkWritePermissions(Path path) throws IOException {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                throw new SecurityException("Path is a directory: " + path);
            }
            if (!Files.isWritable(path)) {
                throw new SecurityException("File is not writable: " + path);
            }
        } else {
            Path parentDir = path.getParent();
            if (parentDir == null || !Files.exists(parentDir)) {
                throw new SecurityException("Parent directory does not exist: " + parentDir);
            }
            if (!Files.isDirectory(parentDir)) {
                throw new SecurityException("Parent path is not a directory: " + parentDir);
            }
            if (!Files.isWritable(parentDir)) {
                throw new SecurityException("Directory is not writable: " + parentDir);
            }
        }
    }
}
