package itmo.lab5.cli.commands;

import java.util.HashMap;
import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.Flat;

/**
 * This class implements the Command interface and provides
 * functionality to display information about the collection.
 * 
 * When executed, this command retrieves the collection from the command context 
 * and returns details about the collection, including its type, the type of its 
 * items, and the number of items.
 */
public class InfoCommand implements Command {
    /**
     * Executes the info command, returning information about the collection of 
     * flats.
     *
     * @param args an array of arguments passed to the command
     * @param context the command context that contains the collection of flats
     * @return a string containing information about the collection, or an error message if the collection cannot be parsed
     */
    @Override
    public String execute(String args[], CommandContext context) {
        var flats = new HashMap<Integer, Flat>();

        try {
            flats = (HashMap<Integer, Flat>) context.get("collection");
        } catch (ClassCastException e) {
            return "Can't parse collection. Something goes wrong!";
        }

        if (null == flats || flats.isEmpty())
            return "Collection is empty now!";

        var anyFlat = flats.values().iterator().next();

        return ("Collections stores in: Information about collection: " + flats.getClass().getName() + "\n" +
                "Collection consists of: " + anyFlat.getClass().getName() + "\n" +
                "Items: " + flats.size());
    }
}