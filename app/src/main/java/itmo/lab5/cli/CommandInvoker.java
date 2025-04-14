package itmo.lab5.cli;

import itmo.lab5.cli.helpers.History;
import itmo.lab5.interfaces.*;

/**
 * Invokes commands from a command registry and maintains command history.
 */
public class CommandInvoker {
  
  private final CommandRegistry registry;
  private final CommandContext context;
  private final History history;

  /**
   * Constructs a CommandInvoker with the specified command registry,
   * context, and history.
   *
   * @param registry the command registry to retrieve commands from.
   * @param context the context to be passed to commands during execution.
   * @param history the history object to track executed commands.
   */
  public CommandInvoker(CommandRegistry registry, CommandContext context, History history) {
    this.registry = registry;
    this.context = context;
    this.history = history;
  }

  /**
   * Executes a command by its name with the provided arguments.
   *
   * @param commandName the name of the command to execute.
   * @param args an array of arguments to pass to the command.
   * @return the result of the command execution, or an error message if
   *         the command is unknown or an exception occurs during execution.
   */
  public String executeCommand(String commandName, String[] args) {
    Command command = registry.getByName(commandName);
    if (command != null) {
      history.add(commandName);
      try {
        return command.execute(args, context);
      } catch (Exception e) {
        return "Error executing command '" + commandName + "': " + e.getMessage();
      }
    }
    return "Unknown command: " + commandName;
  }
}
