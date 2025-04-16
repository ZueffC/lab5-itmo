package itmo.lab5.cli.commands;

import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.*;
import itmo.lab5.models.*;
import itmo.lab5.parser.Writer;
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
    public String execute(String args[], CommandContext context) {
        var writer = new Writer();
        var collection = new HashMap<Integer, Flat>();
        String filePath = null;

        try {
            collection = (HashMap<Integer, Flat>) context.get("collection");
            filePath = (String) context.get("path");
        } catch (ClassCastException e) {
            return "Can't parse collection!";
        }

        if (filePath != null && collection != null)
            return writer.writeCollection(filePath, collection);
        else
            return "Can't parse some object! Check filePath!";
    }
}
