package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.cli.CommandRegistry;
import itmo.lab5.interfaces.Command;

/**
 * This class implements the Command interface and provides
 * functionality to display a list of available commands in the application.
 * 
 * When executed, this command retrieves the command registry from the context 
 * and constructs a string that lists all registered commands. 
 * If no commands are available, it informs user.
 */
public class HelpCommand implements Command {
    
    /**
     * Executes the help command, returning a list of available commands.
     *
     * @param args an array of arguments passed to the command
     * @param context the command context that contains the command registry
     * @return a string listing all available commands, or a message indicating that no commands are available
     */
    @Override
    public String execute(String args[], CommandContext context) {
        var registry = (CommandRegistry) context.get("registry");
        StringBuilder result = new StringBuilder();

        if (null == registry) {
            result.append("There aren't any available commands!");
            return result.toString();
        }

        result.append("List of available commands: ");
        registry.getAllCommands().keySet().forEach(command -> 
                result.append(command).append(", "));

        return result
            .delete(result.length() - 2, result.length())
            .append(".")
            .toString();
    }
}
