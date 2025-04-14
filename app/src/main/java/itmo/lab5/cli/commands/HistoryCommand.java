package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;
import itmo.lab5.cli.helpers.*;

/**
 * This class implements the Command interface and provides
 * functionality to display the command history of the application.
 * 
 * When executed, this command retrieves the history from the command context 
 * and returns a string representation of the command history. If the history is 
 * null or cannot be parsed, an appropriate error message is returned.
 *
 */
public class HistoryCommand implements Command {
    
    /**
     * Executes the history command, returning latest 8 (by design).
     *
     * @param args an array of arguments passed to the command
     * @param context the command context that contains the history of commands
     * @return a string representation of the command history, or an error message if the history cannot be parsed
     */
    @Override
    public String execute(String args[], CommandContext context) {
        var history = new History();

        try {
            history = (History) context.get("history");
        } catch (ClassCastException e) {
            return "There's a problem with history. It might be null :(";
        }

        if (null == history)
            return "We reach the end of history. Somewhere in the world, one Fukuyama is rejoicing :_)";

        return history.toString();
    }
}