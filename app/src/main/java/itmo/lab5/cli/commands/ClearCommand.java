package itmo.lab5.cli.commands;

import java.util.HashMap;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.Flat;

/**
 * This class implements the Command interface and provides
 * functionality to clear the collection of Flat objects.
 * 
 * When executed, this command retrieves the collection from the command context
 * and removes all items from it, effectively emptying the collection.
 */
public class ClearCommand implements Command {

    /**
     * Executes the clear command, removing all items from the collection
     * of flats.
     *
     * @param args    an array of arguments passed to the command
     * @param context the command context that contains the collection of flats
     * @return a message indicating the result of the operation, or an error message
     *         if the collection cannot be parsed
     */
    @Override
    public String execute(String args[], CommandContext context) {
        var collection = new HashMap<Integer, Flat>();

        try {
            collection = (HashMap<Integer, Flat>) context.get("collection");
        } catch (ClassCastException e) {
            return "Can't clear collection. It might be missed.";
        }

        collection.clear();
        return "Collection was successfully cleared!";
    }
}
