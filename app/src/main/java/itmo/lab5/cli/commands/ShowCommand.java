package itmo.lab5.cli.commands;

import java.util.HashMap;

import itmo.lab5.models.Flat;
import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.CommandContext;

/**
 * This class implements the Command interface and provides
 * functionality to display the contents of the collection of Flat objects.
 * 
 * When executed, this command retrieves the collection from the command context 
 * and prints each Flat object to the standard output. If the collection is 
 * empty, it returns a message indicating that there is nothing to show.
 */
public class ShowCommand implements Command {
    
    /**
     * Executes the show command, displaying the contents of the collection
     * of flats.
     *
     * @param args an array of arguments passed to the command
     * @param context the command context that contains the collection of flats
     * @return an empty string if the collection is displayed successfully, or an error message if the collection cannot be parsed
     */
    @Override
    public String execute(String args[], CommandContext context) {
        var collection = new HashMap<Integer, Flat>();

        try {
            collection = (HashMap<Integer, Flat>) context.get("collection");
        } catch (ClassCastException e) {
            return "Can't parse collection!";
        }

        if (collection.isEmpty())
            return "Nothing to show!";

        for (Flat flat : collection.values())
            System.out.println(flat);

        return "";
    }
}
