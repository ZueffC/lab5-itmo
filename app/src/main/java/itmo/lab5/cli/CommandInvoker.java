package itmo.lab5.cli;

import itmo.lab5.cli.helpers.History;
import itmo.lab5.interfaces.*;

public class CommandInvoker {
  private final CommandRegistry registry;
  private final CommandContext context;
  private final History history;

  public CommandInvoker(CommandRegistry registry, CommandContext context, History history) {
    this.registry = registry;
    this.context = context;
    this.history = history;
  }

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
