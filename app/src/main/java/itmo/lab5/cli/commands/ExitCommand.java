package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.interfaces.Command;

/**
 * This class implements the Command interface and provides
 * functionality to terminate the application.
 *
 * When called, main loop will immediately stop.
 */
public class ExitCommand implements Command {

    private static final String description = "command allows to exit REPL";

    public final String toString() {
        return this.description;
    }

    /**
     * Executes the exit command, terminating the application.
     *
     * @param args    an array of arguments passed to the command
     * @param context the command context that may contain additional data
     * @return an empty string
     */
    @Override
    public String execute(String[] args, CommandContext context) {
        System.exit(0);
        return "";
    }
}
