package itmo.lab5.cli.commands;

import itmo.lab5.cli.CommandContext;
import itmo.lab5.cli.CommandRegistry;
import itmo.lab5.interfaces.Command;

public class HelpCommand implements Command {
  @Override
  public String execute(String args[], CommandContext context) {
    var registry = (CommandRegistry) context.get("registry");
    StringBuilder result = new StringBuilder();

    if (registry == null) {
      result.append("There aren't any avaliable commands!");
      return result.toString();
    }

    result.append("List of avaliable commands: ");
    for (String command : registry.getAllCommands().keySet()) {
      result.append(command + ", ");
    }

    return result
        .delete(result.length() - 2, result.length())
        .append(".")
        .toString();
  }
}
