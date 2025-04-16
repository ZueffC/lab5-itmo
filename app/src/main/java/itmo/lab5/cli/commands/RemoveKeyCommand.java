package itmo.lab5.cli.commands;

import java.util.HashMap;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.models.Flat;

/**
 * This class implements the Command interface and provides
 * functionality to remove a object from the collection based on its ID.
 *
 * When executed, this command retrieves the collection from the command
 * context, checks for the specified ID in the arguments, and removes
 * the corresponding object if it exists. If the ID is invalid
 * or the collection is empty, appropriate error messages are returned.
 */
public class RemoveKeyCommand implements Command {
    private static final String description = "command allows to delete collections element by providing element's id";

    public final String toString() {
        return this.description;
    }

    /**
     * Executes the remove key command, deleting a flat by ID.
     *
     * @param args    an array of arguments passed to the command, where the first
     *                element is expected to be the ID of the flat to remove
     * @param context the command context that contains the collection of flats
     * @return a message indicating the result of the operation, or an error message
     *         if the collection cannot be parsed or the ID is invalid
     */
    @Override
    public String execute(String args[], CommandContext context) {
        HashMap<Integer, Flat> flats = new HashMap<>();
        Integer idToDelete = null;

        try {
            flats = (HashMap<Integer, Flat>) context.get("collection");
        } catch (ClassCastException e) {
            return "There's a problem with collection parsing.";
        }

        if (flats == null)
            return "Collection is empty. Can't delete anything :(";

        try {
            idToDelete = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            return "You provided a mistyped argument!";
        }

        if (idToDelete < 0)
            return "Check argument's value twice.";

        if (!flats.containsKey(idToDelete))
            return "Can't find flat with such ID.";

        flats.remove(idToDelete);
        return "Successfully deleted flat!";
    }
}
